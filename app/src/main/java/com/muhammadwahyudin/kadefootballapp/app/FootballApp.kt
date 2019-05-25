package com.muhammadwahyudin.kadefootballapp.app

import android.app.Application
import com.facebook.stetho.Stetho
import com.muhammadwahyudin.kadefootballapp.data.Repository
import com.muhammadwahyudin.kadefootballapp.data.remote.TheSportDbApiService
import com.muhammadwahyudin.kadefootballapp.views.favoritematch.FavoriteMatchViewModel
import com.muhammadwahyudin.kadefootballapp.views.leaguedetail.LeagueDetailViewModel
import com.muhammadwahyudin.kadefootballapp.views.leaguedetail.matchschedule.MatchScheduleViewModel
import com.muhammadwahyudin.kadefootballapp.views.matchdetail.MatchDetailViewModel
import com.muhammadwahyudin.kadefootballapp.views.matchsearch.MatchSearchViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

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

    private val appModule = module {
        single { TheSportDbApiService.create() }
        single { Repository(get()) }
        viewModel { LeagueDetailViewModel(get()) }
        viewModel { MatchDetailViewModel(get()) }
        viewModel { MatchScheduleViewModel(get()) }
        viewModel { FavoriteMatchViewModel(androidContext()) }
        viewModel { MatchSearchViewModel(get()) }

    }
}