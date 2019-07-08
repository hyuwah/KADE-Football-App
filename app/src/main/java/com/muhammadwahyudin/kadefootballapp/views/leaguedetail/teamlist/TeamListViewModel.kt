package com.muhammadwahyudin.kadefootballapp.views.leaguedetail.teamlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.muhammadwahyudin.kadefootballapp.base.BaseViewModel
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.model.*
import io.reactivex.rxkotlin.addTo

class TeamListViewModel(private val repository: IRepository) : BaseViewModel() {
    private var state = MutableLiveData<ResourceState<List<Team>>>(EmptyState())

    fun getTeamList(leagueId: String): LiveData<ResourceState<List<Team>>> {
        state.postValue(LoadingState())
        repository.getTeamList(leagueId)
            .subscribe(
                { teams ->
                    if (teams.isNotEmpty()) state.postValue(PopulatedState(teams))
                    else state.postValue(NoResultState("No team list for $leagueId"))
                },
                { t ->
                    state.postValue(ErrorState(t.localizedMessage))
                }
            )
            .addTo(compDisp)
        return state
    }
}