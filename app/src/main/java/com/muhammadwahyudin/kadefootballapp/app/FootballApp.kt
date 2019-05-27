package com.muhammadwahyudin.kadefootballapp.app

import android.app.Application
import com.facebook.stetho.Stetho
import com.muhammadwahyudin.kadefootballapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class FootballApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        startKoin {
            androidLogger()
            androidContext(this@FootballApp)
            modules(appModule)
        }
    }


}