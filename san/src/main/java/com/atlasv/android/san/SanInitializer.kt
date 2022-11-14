package com.atlasv.android.san

import android.content.Context
import androidx.startup.Initializer
import com.android.atlasv.ad.framework.core.AdManager
import com.android.atlasv.ad.framework.core.IAdFactory

class SanInitializer : Initializer<IAdFactory> {
    override fun create(context: Context): IAdFactory {
        AdManager.addSupportedFactory(SanAdFactory)
        return SanAdFactory
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}