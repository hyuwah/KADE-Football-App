package com.muhammadwahyudin.kadefootballapp.views.leaguedetail.nextmatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.muhammadwahyudin.kadefootballapp.data.Repository
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage
import com.muhammadwahyudin.kadefootballapp.data.remote.TheSportDbApiService

class NextMatchViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val mRepository = Repository(TheSportDbApiService.create())

    private var eventScheduleList = MutableLiveData<List<EventWithImage>>()
    fun getEvents(leagueId: String): LiveData<List<EventWithImage>> {
        eventScheduleList = mRepository.getNextMatchByLeagueId(leagueId)
        return eventScheduleList
    }
}
