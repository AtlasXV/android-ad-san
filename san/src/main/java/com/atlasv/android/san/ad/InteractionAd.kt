package com.atlasv.android.san.ad

import android.app.Activity
import android.content.Context
import com.android.atlasv.ad.framework.core.AdType
import com.android.atlasv.ad.framework.util.AdLog
import com.san.ads.AdError
import com.san.ads.SANInterstitial
import com.san.ads.base.IAdListener
import com.san.ads.core.SANAd

/*
https://github.com/san-sdk/sample/wiki/Interstitial-Ads
 */
class InteractionAd(context: Context, placementId: String) : SanBaseAd(context, placementId),
    IAdListener.AdActionListener,
    IAdListener.AdLoadListener {

    private var interactionAd: SANInterstitial? = null

    override fun doLoad() {
        interactionAd = SANInterstitial(context, placementId)
        interactionAd?.setAdLoadListener(this)
        interactionAd?.load()
    }

    override fun show(activity: Activity): Boolean {
        if (isReady()) {
            interactionAd?.setAdActionListener(this)
            interactionAd?.show()
            AdLog.d(TAG) { "show $placementId" }
            return true
        }
        return false
    }

    override fun getAdType(): Int {
        return AdType.INTERSTITIAL
    }

    override fun onAdImpressionError(error: AdError) {
        onShowFail(error)
    }

    override fun onAdImpression() {
        onShow()
    }

    override fun onAdClicked() {
        onClick()
    }

    override fun onAdCompleted() {
        AdLog.d(TAG) { "onAdCompleted $placementId" }
    }

    override fun onAdClosed(p0: Boolean) {
        onClose()
    }

    override fun onAdLoaded(ad: SANAd?) {
        onLoadSuccess()
    }

    override fun onAdLoadError(error: AdError) {
        onLoadFail(error)
    }

    override fun isReady(): Boolean {
        return checkAdInvalid() && interactionAd?.isAdReady == true
    }
}