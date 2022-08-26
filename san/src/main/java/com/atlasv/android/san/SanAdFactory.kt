package com.atlasv.android.san

import android.app.Activity
import android.content.Context
import com.android.atlasv.ad.framework.ad.BaseAd
import com.android.atlasv.ad.framework.core.AdType
import com.android.atlasv.ad.framework.core.IAdFactory
import com.atlasv.android.san.ad.BannerAd
import com.atlasv.android.san.ad.InteractionAd
import com.atlasv.android.san.ad.RewardVideoAd
import com.san.api.SanAdSdk

class SanAdFactory : IAdFactory() {

    companion object {
        const val PLATFORM = "san"
    }

    override fun buildAd(context: Context, type: Int, adId: String, loadLayoutId: Int): BaseAd? {
        return when (type) {
            AdType.INTERSTITIAL -> InteractionAd(context, adId)
            AdType.BANNER -> BannerAd(context, adId)
            AdType.REWARD -> RewardVideoAd(context, adId)
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
        SanAdSdk.init(context)
    }

    override fun setActivityClass4LoadAds(activityClass: Class<out Activity>) {
    }

    override fun setTestDeviceIds(testDeviceIds: List<String>) {
    }
}