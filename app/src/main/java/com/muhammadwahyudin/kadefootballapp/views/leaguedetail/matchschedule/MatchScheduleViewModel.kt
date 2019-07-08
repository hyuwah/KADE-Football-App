package com.muhammadwahyudin.kadefootballapp.views.leaguedetail.matchschedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.muhammadwahyudin.kadefootballapp.base.BaseViewModel
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.model.*
import io.reactivex.rxkotlin.addTo

class MatchScheduleViewModel(private val repository: IRepository) : BaseViewModel() {
    private val lastEventsState = MutableLiveData<ResourceState<List<EventWithImage>>>(EmptyState())
    private val nextEventsState = MutableLiveData<ResourceState<List<EventWithImage>>>(EmptyState())

    fun getLastEventsState(leagueId: String): LiveData<ResourceState<List<EventWithImage>>> {
        lastEventsState.postValue(LoadingState())
        repository.getLastMatchByLeagueId(leagueId)
            .subscribe(
                { matches ->
                    if (!matches.isNullOrEmpty()) lastEventsState.postValue(PopulatedState(matches))
                    else lastEventsState.postValue(NoResultState("No Previous Match Found"))
                },
                {
                    lastEventsState.postValue(ErrorState(it.localizedMessage))
                }
            )
            .addTo(compDisp)
        return lastEventsState
    }

    fun getNextEventsState(leagueId: String): LiveData<ResourceState<List<EventWithImage>>> {
        nextEventsState.postValue(LoadingState())
        repository.getNextMatchByLeagueId(leagueId)
            .subscribe(
                { matches ->
                    if (!matches.isNullOrEmpty()) nextEventsState.postValue(PopulatedState(matches))
                    else nextEventsState.postValue(NoResultState("No Next Match Found"))
                },
                {
                    nextEventsState.postValue(ErrorState(it.localizedMessage))
                })
            .addTo(compDisp)
        return nextEventsState
    }

}
