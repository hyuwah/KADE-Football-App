package com.muhammadwahyudin.kadefootballapp.views.matchsearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.Repository
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage

class MatchSearchViewModel(private val repository: Repository) : ViewModel() {

    private var resultEventsSchedule = MutableLiveData<List<EventWithImage>>()
    fun fetchEventsByQuery(query: String): LiveData<List<EventWithImage>> {
        resultEventsSchedule = repository.searchMatches(query)
        return resultEventsSchedule
    }
}