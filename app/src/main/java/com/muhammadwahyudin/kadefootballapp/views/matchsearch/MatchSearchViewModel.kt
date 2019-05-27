package com.muhammadwahyudin.kadefootballapp.views.matchsearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.model.*
import com.muhammadwahyudin.kadefootballapp.data.remote.response.SearchEventsRes
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MatchSearchViewModel(private val repository: IRepository) : ViewModel() {

    // StateVM nya ikut mahzab https://medium.com/@oziemowa/android-viewmodel-testing-with-view-states-2a30c461939b
    val state = MutableLiveData<ResourceState<List<EventWithImage>>>().apply {
        value = EmptyState()
    }
    private val compositeDisposable = CompositeDisposable()

    fun searchMatch(query: String) {
        val data = arrayListOf<EventWithImage>()
        state.postValue(LoadingState())
        val disp = repository.searchMatches(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .filter { t: SearchEventsRes ->
                if (t.events.isNullOrEmpty()) state.postValue(NoResultState(query))
                !t.events.isNullOrEmpty()
            }
            .map { t: SearchEventsRes ->
                t.events
            }
            .flattenAsObservable { t: List<EventWithImage> -> t }
            .filter { t ->
                t.strSport.contains("Soccer")
            }
            .flatMapSingle { event ->
                println(event.strEvent)
                repository.updateEventWithTeamBadge(event)
                    .flatMap<EventWithImage> { t: List<String> ->
                        event.strAwayTeamBadge = t[0]
                        event.strHomeTeamBadge = t[1]
                        Single.just(event)
                    }
                    .doOnError { state.postValue(ErrorState(it.localizedMessage)) }
                    .subscribeOn(Schedulers.io())
            }
            .doOnNext { event ->
                println(event.strHomeTeamBadge)
                data.add(event)
                state.postValue(PopulatedState(data.toList()))
            }
            .doOnError { state.postValue(ErrorState(it.localizedMessage)) }
            .subscribe()
        compositeDisposable.add(disp)
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}