package com.muhammadwahyudin.kadefootballapp.views.leaguedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.Repository
import com.muhammadwahyudin.kadefootballapp.data.remote.TheSportDbApiService
import com.muhammadwahyudin.kadefootballapp.data.remote.response.LeagueDetailRes

// TODO Dependency injection with Kodein / Koin
class LeagueDetailViewModel : ViewModel() {
    private val mRepository = Repository(TheSportDbApiService.create())
    private var mLeagueDetail = MutableLiveData<LeagueDetailRes.League>()

    fun getLeagueDetail(id: Int): LiveData<LeagueDetailRes.League> {
        mLeagueDetail = mRepository.getLeagueDetail(id)
        return mLeagueDetail
    }

}