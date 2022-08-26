package com.atlasv.android.san.ad

import android.content.Context
import android.os.Bundle
import com.android.atlasv.ad.framework.ad.BaseAd
import com.android.atlasv.ad.framework.event.AnalysisEvent
import com.android.atlasv.ad.framework.event.AnalysisStatus
import com.android.atlasv.ad.framework.event.EventAgent
import com.android.atlasv.ad.framework.util.AdLog
import com.atlasv.android.san.SanAdFactory
import com.san.ads.AdError

abstract class SanBaseAd(val context: Context, val placementId: String) :
    BaseAd() {
    companion object {
        const val CACHE_DURATION = 60 * 60 * 1000 //60min
    }

    protected val TAG by lazy { "sanAd(${getAdType()})" }
    private var isLoading = false
    private var retryable = true
    private var clicked = false
    private var clickedTimestamp = System.currentTimeMillis()
    private var loadTimestamp = System.currentTimeMillis()

    override fun getAdPlatform(): String {
        return SanAdFactory.PLATFORM
    }

    override fun prepare() {
        if (isLoading) return
        if (isReady()) return
        isLoading = true
        doLoad()
        EventAgent.logEvent(
            context.applicationContext,
            AnalysisEvent.AD_LOAD,
            ofBundle()
        )
        AdLog.d(TAG) { "load $placementId" }
    }

    override fun onResume() {
        super.onResume()
        if (clicked) {
            val bundle = ofBundle()
            val exitDuration = System.currentTimeMillis() - clickedTimestamp
            bundle.putLong("duration", exitDuration)
            EventAgent.logEvent(
                context.applicationContext,
                AnalysisEvent.AD_BACK,
                bundle
            )
            clicked = false
            AdLog.d(TAG) { "onAdClickBack $placementId" }
        }
    }

    protected abstract fun doLoad()

    protected fun onLoadSuccess() {
        AdLog.d(TAG) { "onAdLoaded $placementId" }

        isLoading = false
        loadTimestamp = System.currentTimeMillis()
        EventAgent.logEvent(
            context.applicationContext,
            AnalysisEvent.AD_LOAD_SUCCESS,
            ofBundle()
        )
        adListener?.onAdLoaded(this)
    }

    protected fun checkAdInvalid(): Boolean {
        return System.currentTimeMillis() - loadTimestamp < CACHE_DURATION
    }

    protected fun onLoadFail(adError: AdError) {
        AdLog.d(TAG) { "onAdLoadError $placementId, $adError" }

        isLoading = false
        val bundle = ofBundle()
        bundle.putInt("errorCode", adError.errorCode)
        EventAgent.logEvent(
            context.applicationContext,
            AnalysisEvent.AD_LOAD_FAIL,
            bundle
        )
        adListener?.onAdFailedToLoad(adError.errorCode)

        if (adError == AdError.NETWORK_ERROR && retryable) {
            retryable = false
            doLoad()
            EventAgent.logEvent(
                context.applicationContext,
                AnalysisEvent.AD_FAILED_RETRY,
                ofBundle()
            )
        }
    }

    protected fun onShow() {
        AdLog.d(TAG) { "onAdImpression $placementId" }
        EventAgent.logEvent(
            context.applicationContext,
            AnalysisEvent.AD_IMPRESSION,
            ofBundle()
        )
        adListener?.onAdOpened()
        adListener?.onAdImpression()
    }

    protected fun onNotShow() {
        if (isLoading) {
            EventAgent.logAdShow(
                context.applicationContext,
                placementId,
                false,
                AnalysisStatus.LOAD_NOT_COMPLETED.getValue()
            )
        } else {
            EventAgent.logAdShow(
                context.applicationContext,
                placementId,
                false,
                AnalysisStatus.LOAD_FAILED.getValue()
            )
        }
    }

    protected fun onShowFail(error: AdError) {
        AdLog.d(TAG) { "onAdImpressionError $placementId, $error" }
        val bundle = ofBundle()
        bundle.putInt("errorCode", error.errorCode)
        EventAgent.logEvent(
            context.applicationContext,
            AnalysisEvent.AD_FAILED_TO_SHOW,
            bundle
        )
    }

    protected fun onClick() {
        AdLog.d(TAG) { "onAdClicked $placementId" }
        EventAgent.logEvent(
            context.applicationContext,
            AnalysisEvent.AD_CLICK,
            ofBundle()
        )
        clicked = true
        clickedTimestamp = System.currentTimeMillis()

        adListener?.onAdClicked()
    }

    protected fun onClose() {
        AdLog.d(TAG) { "onAdClosed $placementId" }
        EventAgent.logEvent(
            context.applicationContext,
            AnalysisEvent.AD_CLOSE,
            ofBundle()
        )
        adListener?.onAdClosed()
    }

    private fun ofBundle(): Bundle {
        return Bundle().apply { putString("placement", placementId) }
    }
}