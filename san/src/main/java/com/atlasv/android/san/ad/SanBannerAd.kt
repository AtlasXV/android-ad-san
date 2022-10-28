package com.atlasv.android.san.ad

import android.content.Context
import android.view.Gravity
import android.view.View
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
class SanBannerAd(context: Context, adId: String, private val adSize: AdSize? = null) :
    SanBaseAd(context, adId),
    IAdListener.AdLoadListener, IAdListener.AdActionListener {
    private var bannerAd: SANBanner? = null

    override fun doLoad() {
        bannerAd = SANBanner(context, adId)
        val bannerAdSize = adSize ?: AdSize.BANNER
        bannerAd?.adSize = bannerAdSize
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

    override fun show(container: ViewGroup, lp: ViewGroup.LayoutParams) {
        val view = bannerAd?.adView ?: return
        if (isReady()) {
            container.visibility = View.VISIBLE
            container.removeAllViews()
            if (view.parent != null && view.parent is ViewGroup) {
                (view.parent as ViewGroup).removeView(view)
            }
            bannerAd?.setAdActionListener(this)
            container.addView(view, lp)
        }
    }

    override fun onAdLoaded(p0: SANAd?) {
        onLoadSuccess()
    }

    override fun onAdLoadError(error: AdError) {
        onLoadFail(error)
    }

    override fun isReady(): Boolean {
        return bannerAd?.isAdReady == true
    }

    override fun onAdImpressionError(error: AdError) {

    }

    override fun onAdImpression() {
        onShow()
    }

    override fun onAdClicked() {
        onClick()
    }

    override fun onAdCompleted() {

    }

    override fun onAdClosed(p0: Boolean) {

    }

    override fun getAdType(): Int {
        return AdType.BANNER
    }

    override fun onResume() {
        AdLog.w(TAG) { "onResume $placement $adId" }
    }

    override fun onPause() {
        AdLog.w(TAG) { "onPause $placement $adId" }
    }

    override fun onDestroy() {
        AdLog.w(TAG) { "onDestroy $placement $adId" }
        bannerAd?.destroy()
        bannerAd = null
    }
}