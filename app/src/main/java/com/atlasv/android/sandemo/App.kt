package com.atlasv.android.sandemo

import android.app.Application
import com.android.atlasv.ad.framework.core.AdManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        TestAdHelper.init(this)
        AdManager.initializePlatformSdk(this)
    }
}