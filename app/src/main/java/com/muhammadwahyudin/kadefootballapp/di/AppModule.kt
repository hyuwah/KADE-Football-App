package com.muhammadwahyudin.kadefootballapp.di

import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.Repository
import com.muhammadwahyudin.kadefootballapp.data.remote.TheSportDbApiService
import com.muhammadwahyudin.kadefootballapp.views.favoritematch.FavoriteMatchViewModel
import com.muhammadwahyudin.kadefootballapp.views.leaguedetail.LeagueDetailViewModel
import com.muhammadwahyudin.kadefootballapp.views.leaguedetail.matchschedule.MatchScheduleViewModel
import com.muhammadwahyudin.kadefootballapp.views.matchdetail.MatchDetailViewModel
import com.muhammadwahyudin.kadefootballapp.views.matchsearch.MatchSearchViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { TheSportDbApiService.create() }
    single { Repository(Schedulers.io(), AndroidSchedulers.mainThread(), get()) as IRepository }
    viewModel { LeagueDetailViewModel(get()) }
    viewModel { MatchDetailViewModel(get()) }
    viewModel { MatchScheduleViewModel(get()) }
    viewModel { FavoriteMatchViewModel(androidContext(), get()) }
    viewModel { MatchSearchViewModel(get()) }

}