package com.muhammadwahyudin.kadefootballapp.views.search

import androidx.lifecycle.MutableLiveData
import com.muhammadwahyudin.kadefootballapp.base.BaseViewModel
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.model.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class SearchViewModel(private val repo: IRepository) : BaseViewModel() {
    val matchState = MutableLiveData<ResourceState<List<EventWithImage>>>(EmptyState())
    val teamState = MutableLiveData<ResourceState<List<Team>>>(EmptyState())

    fun searchMatch(query: String) {
        val data = arrayListOf<EventWithImage>()
        matchState.postValue(LoadingState())
        repo.searchMatches(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter { t ->
                if (t.events.isNullOrEmpty()) matchState.postValue(NoResultState(query))
                !t.events.isNullOrEmpty()
            }
            .map { t -> t.events }
            .flattenAsObservable { t -> t }
            .filter { t -> t.strSport.contains("Soccer") }
            .flatMapSingle { event ->
                repo.updateEventWithTeamBadge(event)
                    .flatMap { t ->
                        event.strAwayTeamBadge = t[0]
                        event.strHomeTeamBadge = t[1]
                        Single.just(event)
                    }
                    .doOnError { matchState.postValue(ErrorState(it.localizedMessage)) }
                    .subscribeOn(Schedulers.io())
            }
            .subscribe(
                { event ->
                    data.add(event)
                    matchState.postValue(PopulatedState(data.toList()))
                },
                {
                    matchState.postValue(ErrorState(it.localizedMessage))
                }
            )
            .addTo(compDisp)
    }

    fun searchTeam(query: String) {
        val data = arrayListOf<Team>()
        teamState.postValue(LoadingState())
        repo.searchTeams(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter { t ->
                if (t.teams.isNullOrEmpty()) matchState.postValue(NoResultState(query))
                !t.teams.isNullOrEmpty()
            }
            .map { t -> t.teams }
            .flattenAsObservable { t -> t }
            .filter { t -> t.strSport!!.contains("Soccer") }
            .subscribe(
                { team ->
                    data.add(team)
                    teamState.postValue(PopulatedState(data.toList()))
                },
                {
                    teamState.postValue(ErrorState(it.localizedMessage))
                })
            .addTo(compDisp)
    }

}