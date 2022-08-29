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
class RewardVideoAd(context: Context, placementId: String) : SanBaseAd(context, placementId),
    IAdListener.AdLoadListener {

    private var rewardAd: SANReward? = null

    override fun doLoad() {
        rewardAd = SANReward(context, placementId)
        rewardAd?.setAdLoadListener(this)
        rewardAd?.load()
    }

    fun show(activity: Activity, onGainReward: (() -> Unit)? = null) {
        if (isReady()) {
            rewardAd?.setAdActionListener(object : IAdListener.AdActionListener {
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
                    onGainReward?.invoke()
                }

                override fun onAdClosed(p0: Boolean) {
                    onClose()
                }
            })
            rewardAd?.show()
            AdLog.d(TAG) { "show $placementId" }
        } else {
            onNotShow()
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
        return checkAdInvalid() && rewardAd?.isAdReady == true
    }

}