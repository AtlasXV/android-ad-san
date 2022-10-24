package com.atlasv.android.san.ad

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.android.atlasv.ad.framework.core.AdType
import com.android.atlasv.ad.framework.util.AdLog
import com.san.ads.AdError
import com.san.ads.AdSize
import com.san.ads.SANBanner
import com.san.ads.base.IAdListener
import com.san.ads.core.SANAd

/*
https://github.com/san-sdk/sample/wiki/Banner-Ads
 */
class BannerAd(context: Context, adId: String) : SanBaseAd(context, adId),
    IAdListener.AdLoadListener, IAdListener.AdActionListener {
    private var bannerAd: SANBanner? = null

    override fun doLoad() {
        bannerAd = SANBanner(context, adId)
        bannerAd?.adSize = AdSize.MEDIUM_RECTANGLE
        bannerAd?.setAdLoadListener(this)
        bannerAd?.load()
    }

    override fun show(container: ViewGroup, layoutId: Int): Boolean {
        val view = bannerAd?.adView

        if (isReady() && view != null) {
            bannerAd?.setAdActionListener(this)
            if (container.childCount != 0) {
                container.removeAllViews()
            }
            val frameLayout = FrameLayout(container.context)
            val lp = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
            frameLayout.addView(view, lp)
            container.addView(
                frameLayout,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
            return true
        }
        return false
    }

    override fun onAdLoaded(p0: SANAd?) {
        onLoadSuccess()
    }

    override fun onAdLoadError(error: AdError) {
        onLoadFail(error)
    }

    override fun isReady(): Boolean {
        return checkAdInvalid() && bannerAd?.isAdReady == true
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
        AdLog.d(TAG) { "onAdCompleted $adId" }
    }

    override fun onAdClosed(p0: Boolean) {
        onClose()
        bannerAd?.destroy()
        bannerAd = null
    }

    override fun getAdType(): Int {
        return AdType.BANNER
    }
}