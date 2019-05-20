package com.muhammadwahyudin.kadefootballapp.views.matchsearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.Repository
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage
import com.muhammadwahyudin.kadefootballapp.data.remote.TheSportDbApiService

class MatchSearchViewModel : ViewModel() {
    // TODO Ambil dari DI / Kodein
    private val mRepository = Repository(TheSportDbApiService.create())

    private var resultEventsSchedule = MutableLiveData<List<EventWithImage>>()
    fun fetchEventsByQuery(query: String): LiveData<List<EventWithImage>> {
        resultEventsSchedule = mRepository.searchMatches(query)
        return resultEventsSchedule
    }
}