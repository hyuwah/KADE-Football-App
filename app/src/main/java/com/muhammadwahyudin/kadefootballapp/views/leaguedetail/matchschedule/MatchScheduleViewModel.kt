package com.muhammadwahyudin.kadefootballapp.views.leaguedetail.matchschedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.Repository
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage
import com.muhammadwahyudin.kadefootballapp.data.remote.TheSportDbApiService

class MatchScheduleViewModel : ViewModel() {
    // TODO Ambil dari DI / Kodein
    private val mRepository = Repository(TheSportDbApiService.create())

    private var lastEventsSchedule = MutableLiveData<List<EventWithImage>>()

    fun getLastEvents(leagueId: String): LiveData<List<EventWithImage>> {
        lastEventsSchedule = mRepository.getLastMatchByLeagueId(leagueId)
        return lastEventsSchedule
    }

    private var nextEventsSchedule = MutableLiveData<List<EventWithImage>>()
    fun getNextEvents(leagueId: String): LiveData<List<EventWithImage>> {
        nextEventsSchedule = mRepository.getNextMatchByLeagueId(leagueId)
        return nextEventsSchedule
    }

}
