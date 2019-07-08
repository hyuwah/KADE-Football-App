package com.muhammadwahyudin.kadefootballapp.views.leaguedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.muhammadwahyudin.kadefootballapp.base.BaseViewModel
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.model.*
import com.muhammadwahyudin.kadefootballapp.data.remote.response.LeagueDetailRes
import io.reactivex.rxkotlin.addTo

class LeagueDetailViewModel(private val repository: IRepository) : BaseViewModel() {

    private var state = MutableLiveData<ResourceState<LeagueDetailRes.League>>(EmptyState())

    fun getLeagueDetail(id: Int): LiveData<ResourceState<LeagueDetailRes.League>> {
        state.postValue(LoadingState())
        repository.getLeagueDetail(id)
            .subscribe(
                {
                    state.postValue(PopulatedState(it))
                },
                {
                    state.postValue(ErrorState(it.localizedMessage))
                }
            )
            .addTo(compDisp)
        return state
    }

}