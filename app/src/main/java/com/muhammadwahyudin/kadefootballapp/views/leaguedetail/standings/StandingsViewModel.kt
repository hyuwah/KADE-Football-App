package com.muhammadwahyudin.kadefootballapp.views.leaguedetail.standings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.model.*

class StandingsViewModel(private val repository: IRepository) : ViewModel() {

    private var state = MutableLiveData<ResourceState<List<Standing>>>(EmptyState())

    fun getLeagueStandings(leagueId: String): LiveData<ResourceState<List<Standing>>> {
        state.postValue(LoadingState())
        return Transformations.switchMap(repository.getLeagueStandings(leagueId)) {
            if (it.isNullOrEmpty())
                state.postValue(NoResultState("No standings data for $leagueId"))
            else
                state.postValue(PopulatedState(it))
            state
        }
    }
}