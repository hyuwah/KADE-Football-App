package com.muhammadwahyudin.kadefootballapp.views.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.model.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SearchViewModel(private val repo: IRepository) : ViewModel() {
    val matchState = MutableLiveData<ResourceState<List<EventWithImage>>>(EmptyState())
    val teamState = MutableLiveData<ResourceState<List<Team>>>(EmptyState())
    private val compositeDisposable = CompositeDisposable()

    fun searchMatch(query: String) {
        val data = arrayListOf<EventWithImage>()
        matchState.postValue(LoadingState())
        val disp = repo.searchMatches(query)
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
            .doOnNext { event ->
                data.add(event)
                matchState.postValue(PopulatedState(data.toList()))
            }
            .doOnError { matchState.postValue(ErrorState(it.localizedMessage)) }
            .subscribe()
        compositeDisposable.add(disp)
    }

    fun searchTeam(query: String) {
        val data = arrayListOf<Team>()
        teamState.postValue(LoadingState())
        val disp = repo.searchTeams(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter { t ->
                if (t.teams.isNullOrEmpty()) matchState.postValue(NoResultState(query))
                !t.teams.isNullOrEmpty()
            }
            .map { t -> t.teams }
            .flattenAsObservable { t -> t }
            .filter { t -> t.strSport!!.contains("Soccer") }
            .doOnNext { team ->
                data.add(team)
                teamState.postValue(PopulatedState(data.toList()))
            }
            .doOnError { teamState.postValue(ErrorState(it.localizedMessage)) }
            .subscribe()
        compositeDisposable.add(disp)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}