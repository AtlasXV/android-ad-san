package com.atlasv.android.sandemo

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import com.android.atlasv.ad.framework.core.AdType
import com.atlasv.android.sandemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val interactionAdResult = MutableLiveData<String>()
    val bannerAdResult = MutableLiveData<String>()
    val rewardAdResult = MutableLiveData<String>()
    private lateinit var dataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        dataBinding.activity = this
        TestAdHelper.build(this)
    }

    fun loadNative() {
        TestAdHelper.getAd(TestAdHelper.adIdList[AdType.NATIVE]!!)?.prepare()
    }

    fun showNative() {
        TestAdHelper.getAd(TestAdHelper.adIdList[AdType.NATIVE]!!)
            ?.show(dataBinding.nativeCard, R.layout.general_native_ad_layout)
    }

    fun loadInteraction() {
        TestAdHelper.getAd(TestAdHelper.adIdList[AdType.INTERSTITIAL]!!)?.prepare()
    }

    fun showInteraction() {
        TestAdHelper.getAd(TestAdHelper.adIdList[AdType.INTERSTITIAL]!!)?.show(this)

    }

    fun loadBanner() {
        TestAdHelper.getAd(TestAdHelper.adIdList[AdType.BANNER]!!)?.prepare()

    }

    fun showBanner() {
        val lp = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        )
        TestAdHelper.getAd(TestAdHelper.adIdList[AdType.BANNER]!!)
            ?.show(dataBinding.flBanner, lp)

    }

    fun loadReward() {
        TestAdHelper.getAd(TestAdHelper.adIdList[AdType.REWARD]!!)?.prepare()

    }

    fun showReward() {
        TestAdHelper.getAd(TestAdHelper.adIdList[AdType.REWARD]!!)?.show(this) {
            Log.d("sanAd(Reward)", "onRewarded")
        }
    }
}