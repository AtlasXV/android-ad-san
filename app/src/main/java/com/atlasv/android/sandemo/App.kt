package com.atlasv.android.sandemo

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        TestAdHelper.init(this)
    }
}