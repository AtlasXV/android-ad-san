package com.atlasv.android.sandemo

import android.os.Bundle
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
        TestAdHelper.getAd(TestAdHelper.adIdList[AdType.BANNER]!!)
            ?.show(dataBinding.flBanner, R.id.flBanner)

    }

    fun loadReward() {
        TestAdHelper.getAd(TestAdHelper.adIdList[AdType.REWARD]!!)?.prepare()

    }

    fun showReward() {
        TestAdHelper.getAd(TestAdHelper.adIdList[AdType.REWARD]!!)?.show(this)

    }
}