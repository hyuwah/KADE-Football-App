package com.muhammadwahyudin.kadefootballapp.views.leaguedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.remote.response.LeagueDetailRes

class LeagueDetailViewModel(private val repository: IRepository) : ViewModel() {
    private var mLeagueDetail = MutableLiveData<LeagueDetailRes.League>()

    fun getLeagueDetail(id: Int): LiveData<LeagueDetailRes.League> {
        mLeagueDetail = repository.getLeagueDetail(id)
        return mLeagueDetail
    }

}