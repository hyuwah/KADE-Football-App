package com.muhammadwahyudin.kadefootballapp.views.leaguedetail.matchschedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.model.*

class MatchScheduleViewModel(private val repository: IRepository) : ViewModel() {
    private val lastEventsState = MutableLiveData<ResourceState<List<EventWithImage>>>(EmptyState())
    private val nextEventsState = MutableLiveData<ResourceState<List<EventWithImage>>>(EmptyState())

    fun getLastEventsState(leagueId: String): LiveData<ResourceState<List<EventWithImage>>> {
        lastEventsState.postValue(LoadingState())
        return Transformations.switchMap(repository.getLastMatchByLeagueId(leagueId)) {
            if (it.isNullOrEmpty()) {
                lastEventsState.postValue(NoResultState("No previous match for $leagueId"))
            } else {
                lastEventsState.postValue(PopulatedState(it))
            }
            lastEventsState
        }
    }

    fun getNextEventsState(leagueId: String): LiveData<ResourceState<List<EventWithImage>>> {
        nextEventsState.postValue(LoadingState())
        return Transformations.switchMap(repository.getNextMatchByLeagueId(leagueId)) {
            if (it.isNullOrEmpty()) {
                nextEventsState.postValue(NoResultState("No previous match for $leagueId"))
            } else {
                nextEventsState.postValue(PopulatedState(it))
            }
            nextEventsState
        }
    }

}
