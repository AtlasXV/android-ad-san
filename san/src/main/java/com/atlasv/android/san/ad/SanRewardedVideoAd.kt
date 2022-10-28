package com.atlasv.android.san.ad

import android.app.Activity
import android.content.Context
import com.android.atlasv.ad.framework.core.AdType
import com.android.atlasv.ad.framework.util.AdLog
import com.san.ads.AdError
import com.san.ads.SANReward
import com.san.ads.base.IAdListener
import com.san.ads.core.SANAd

/*
https://github.com/san-sdk/sample/wiki/Rewarded-Video-Ads
 */
class SanRewardedVideoAd(context: Context, adId: String) : SanBaseAd(context, adId),
    IAdListener.AdLoadListener {

    private var rewardAd: SANReward? = null
    private var sanRewardedAction: (() -> Unit)? = null

    override fun doLoad() {
        rewardAd = SANReward(context, adId)
        rewardAd?.setAdLoadListener(this)
        rewardAd?.setAdActionListener(object : IAdListener.AdActionListener {
            override fun onAdImpressionError(error: AdError) {
                onShowFail(error)
                sanRewardedAction = null
            }

            override fun onAdImpression() {
                onShow()
            }

            override fun onAdClicked() {
                onClick()
            }

            override fun onAdCompleted() {
                AdLog.d(TAG) { "onAdCompleted $adId" }
                sanRewardedAction?.invoke()
                sanRewardedAction = null
            }

            override fun onAdClosed(p0: Boolean) {
                onClose()
                sanRewardedAction = null
                rewardAd?.destroy()
                rewardAd = null
            }
        })
        rewardAd?.load()
    }

    override fun show(activity: Activity, rewardedAction: () -> Unit): Boolean {
        return if (isReady()) {
            sanRewardedAction = rewardedAction
            rewardAd?.show()
            AdLog.d(TAG) { "show $adId" }
            true
        } else {
            onNotShow()
            false
        }
    }

    override fun getAdType(): Int {
        return AdType.REWARD
    }

    override fun onAdLoaded(ad: SANAd?) {
        onLoadSuccess()
    }

    override fun onAdLoadError(error: AdError) {
        onLoadFail(error)
    }

    override fun isReady(): Boolean {
        return rewardAd?.isAdReady == true
    }

}