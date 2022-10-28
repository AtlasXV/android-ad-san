package com.atlasv.android.san

import android.app.Activity
import android.content.Context
import com.android.atlasv.ad.framework.ad.BaseAd
import com.android.atlasv.ad.framework.core.AdType
import com.android.atlasv.ad.framework.core.IAdFactory
import com.atlasv.android.san.ad.SanBannerAd
import com.atlasv.android.san.ad.SanInterstitialAd
import com.atlasv.android.san.ad.SanRewardedVideoAd
import com.san.ads.AdSize
import com.san.api.SanAdSdk

class SanAdFactory : IAdFactory() {

    companion object {
        const val PLATFORM = "san"
    }

    override fun buildAd(context: Context, type: Int, adId: String, loadLayoutId: Int, inlineBanner: Boolean): BaseAd? {
        return when (type) {
            AdType.INTERSTITIAL -> SanInterstitialAd(context, adId)
            AdType.BANNER -> {
                val adSize = if (inlineBanner) AdSize.MEDIUM_RECTANGLE else AdSize.BANNER
                SanBannerAd(context, adId, adSize)
            }
            AdType.REWARD -> SanRewardedVideoAd(context, adId)
            else -> null
        }
    }

    override fun getAdPlatform(): String {
        return PLATFORM
    }

    /*
     https://github.com/san-sdk/sample/wiki/Initialize
     */
    override fun initializePlatformSdk(context: Context) {
        SanAdSdk.notifyConsentStatus(context, true)
        SanAdSdk.init(context)
    }

    override fun setActivityClasses4LoadAds(activitySet: Set<Class<out Activity>>) {
    }

    override fun setTestDeviceIds(testDeviceIds: List<String>) {
    }
}