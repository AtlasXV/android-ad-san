package com.atlasv.android.sandemo

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.android.atlasv.ad.framework.ad.BaseAd
import com.android.atlasv.ad.framework.core.AdManager
import com.android.atlasv.ad.framework.core.AdType
import com.android.atlasv.ad.framework.event.AnalyticsListener
import com.android.atlasv.ad.framework.util.AdLog
import com.atlasv.android.san.ad.SanBannerAd
import com.san.ads.AdSize

object TestAdHelper {
    const val TAG = "TestAdHelper"
    private const val PLATFORM = "san"
    private const val ID_INTERSTITIAL = "1752"
    private const val ID_REWARDED = "1753"
    private const val ID_BANNER = "1754"
    private const val ID_BANNER250 = "1751"
    private const val ID_NATIVE = "1769"

    private val adList = mutableMapOf<String, BaseAd?>()
    val adIdList: Map<Int, String> = mapOf(
        AdType.NATIVE to ID_NATIVE,
        AdType.INTERSTITIAL to ID_INTERSTITIAL,
        AdType.BANNER to ID_BANNER250,
        AdType.REWARD to ID_REWARDED
    )

    fun init(context: Context) {
        if (BuildConfig.DEBUG) {
            AdLog.setLogLevel(Log.VERBOSE)
        }
        AdManager.setAnalyticsListener(object : AnalyticsListener {
            override fun logEvent(event: String, bundle: Bundle?) {
                Log.d(TAG, "logEvent: ${event}. $bundle")
            }
        })
    }

    fun build(context: Context) {
        adList.clear()
        adIdList.forEach {
            val (type, adId) = it
            val inlineBanner = adId == ID_BANNER250
            adList[adId] = AdManager.buildAd(
                context = context, type = type, adId = adId,
                platform = PLATFORM, inlineBanner = inlineBanner)
        }
    }

    fun getAd(adId: String): BaseAd? {
        return adList[adId]
    }
}