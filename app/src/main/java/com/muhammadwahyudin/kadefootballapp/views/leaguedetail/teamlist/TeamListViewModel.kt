package com.muhammadwahyudin.kadefootballapp.views.leaguedetail.teamlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.model.*

class TeamListViewModel(private val repository: IRepository) : ViewModel() {
    private var state = MutableLiveData<ResourceState<List<Team>>>(EmptyState())
    fun getTeamList(leagueId: String): LiveData<ResourceState<List<Team>>> {
        state.postValue(LoadingState())
        return Transformations.switchMap(repository.getTeamList(leagueId)) {

            if (it.isNullOrEmpty())
                state.postValue(NoResultState("No team list for $leagueId"))
            else
                state.postValue(PopulatedState(it))

            state
        }
    }
}