package com.atlasv.android.san

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.android.atlasv.ad.framework.ad.BaseAd
import com.android.atlasv.ad.framework.core.AdType
import com.android.atlasv.ad.framework.core.IAdFactory
import com.android.atlasv.ad.framework.util.AdLog
import com.atlasv.android.san.ad.SanBannerAd
import com.atlasv.android.san.ad.SanInterstitialAd
import com.atlasv.android.san.ad.SanNativeAd
import com.atlasv.android.san.ad.SanRewardedVideoAd
import com.san.ads.AdSize
import com.san.api.SanAdSdk


object SanAdFactory : IAdFactory() {

    const val PLATFORM = "san"
    private var startInitialize = false

    override fun buildAd(
        context: Context,
        type: Int,
        adId: String,
        loadLayoutId: Int,
        inlineBanner: Boolean
    ): BaseAd? {
        return when (type) {
            AdType.NATIVE -> SanNativeAd(context, adId)
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
        if (!startInitialize) {
            startInitialize = true
            try {
                val provider = ComponentName(context, "com.san.core.WeakIniter")
                context.packageManager.setComponentEnabledSetting(
                    provider, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
                SanAdSdk.notifyConsentStatus(context, true)
                SanAdSdk.init(context)
            } catch (e: Throwable) {
                AdLog.e("sanAd", { "initialize exception" }, { e })
            }
        }
    }

    override fun setActivityClasses4LoadAds(activitySet: Set<Class<out Activity>>) {
    }

    override fun setTestDeviceIds(testDeviceIds: List<String>) {
    }

}