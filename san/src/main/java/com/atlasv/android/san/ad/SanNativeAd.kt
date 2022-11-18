package com.atlasv.android.san.ad

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.atlasv.ad.framework.core.AdType
import com.android.atlasv.ad.framework.util.AdLog
import com.atlasv.android.san.R
import com.san.ads.AdError
import com.san.ads.MediaView
import com.san.ads.SANNativeAd
import com.san.ads.base.IAdListener
import com.san.ads.core.SANAd
import com.san.ads.render.AdViewRenderHelper

/**
 * https://github.com/san-sdk/sample/wiki/Native-Ads
 */
class SanNativeAd(context: Context, adId: String) : SanBaseAd(context, adId),
    IAdListener.AdLoadListener, IAdListener.AdActionListener {
    private var nativeAd: SANNativeAd? = null

    override fun doLoad() {
        nativeAd = SANNativeAd(context, adId)
        nativeAd?.setAdLoadListener(this)
        nativeAd?.load()
    }

    override fun onAdLoaded(ad: SANAd?) {
        onLoadSuccess()
    }

    override fun onAdLoadError(error: AdError) {
        onLoadFail(error)
    }

    override fun show(container: ViewGroup, layoutId: Int): Boolean {
        if (isReady()) {
            isShowing = true
            nativeAd?.setAdActionListener(this)
            AdLog.d(TAG) { "show $adId" }
            val adContainer = LayoutInflater.from(context).inflate(layoutId, container, true)
            renderAdContent(adContainer, nativeAd!!)
        }
        return true
    }

    private fun renderAdContent(container: View, ad: SANNativeAd) {
        val icon = container.findViewById<ImageView>(R.id.icon)
        val title = container.findViewById<TextView>(R.id.headline)
        val body = container.findViewById<TextView>(R.id.body)
        val button = container.findViewById<TextView>(R.id.callToAction)
        val mediaLayout = container.findViewById<ViewGroup>(R.id.media)
        AdViewRenderHelper.loadImage(context, ad.iconUrl, icon)
        title.text = ad.title
        body.text = ad.content
        button.text = ad.callToAction
         val mediaView = MediaView(context)
        mediaLayout.addView(
            mediaView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mediaView.loadMadsMediaView(ad.nativeAd)
        val clickableViews = mutableListOf(
            icon, title, body, button, mediaLayout
        )
        ad.prepare(container, clickableViews, null)
    }

    override fun isReady(): Boolean {
        return nativeAd?.isAdReady == true
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

    override fun getAdType(): Int {
        return AdType.NATIVE
    }

    override fun onAdClosed(p0: Boolean) {
        isShowing = false
        onClose()
        nativeAd?.destroy()
        nativeAd = null
    }
}