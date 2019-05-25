package com.muhammadwahyudin.kadefootballapp.views.leaguedetail.matchschedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.Repository
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage

class MatchScheduleViewModel(private val repository: Repository) : ViewModel() {
    private var lastEventsSchedule = MutableLiveData<List<EventWithImage>>()
    private var nextEventsSchedule = MutableLiveData<List<EventWithImage>>()

    fun getLastEvents(leagueId: String): LiveData<List<EventWithImage>> {
        lastEventsSchedule = repository.getLastMatchByLeagueId(leagueId)
        return lastEventsSchedule
    }

    fun getNextEvents(leagueId: String): LiveData<List<EventWithImage>> {
        nextEventsSchedule = repository.getNextMatchByLeagueId(leagueId)
        return nextEventsSchedule
    }

}
