package com.parassidhu.unsplashsample

import android.app.Application
import com.facebook.stetho.Stetho
import com.parassidhu.unsplashsample.di.listOfModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class UnsplashApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
        initStetho()
    }

    private fun initStetho() {
        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this)
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@UnsplashApplication)
            modules(listOfModules)
        }
    }
}