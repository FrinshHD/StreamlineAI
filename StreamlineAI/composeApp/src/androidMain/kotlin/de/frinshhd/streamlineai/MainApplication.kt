package de.frinshhd.streamlineai

import android.app.Application
import de.frinshhd.streamlineai.di.platformModule
import de.frinshhd.streamlineai.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Koin with shared + platform modules
        startKoin {
            androidContext(this@MainApplication)
            modules(sharedModule, platformModule)
        }
    }
}