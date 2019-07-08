package com.muhammadwahyudin.kadefootballapp.views.leaguedetail.standings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.muhammadwahyudin.kadefootballapp.base.BaseViewModel
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.model.*
import io.reactivex.rxkotlin.addTo

class StandingsViewModel(private val repository: IRepository) : BaseViewModel() {

    private var state = MutableLiveData<ResourceState<List<Standing>>>(EmptyState())

    fun getLeagueStandings(leagueId: String): LiveData<ResourceState<List<Standing>>> {
        state.postValue(LoadingState())
        repository.getLeagueStandings(leagueId)
            .subscribe(
                {
                    if (!it.isNullOrEmpty()) state.postValue(PopulatedState(it))
                    else state.postValue(NoResultState("No standings data gor ${leagueId}"))
                },
                {
                    state.postValue(ErrorState(it.localizedMessage))
                }
            )
            .addTo(compDisp)
        return state
    }
}