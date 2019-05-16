package com.muhammadwahyudin.kadefootballapp.views.leaguedetail.lastmatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.Repository
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage
import com.muhammadwahyudin.kadefootballapp.data.remote.TheSportDbApiService

class LastMatchViewModel : ViewModel() {
    // TODO Ambil dari DI / Kodein
    private val mRepository = Repository(TheSportDbApiService.create())

    private var eventScheduleList = MutableLiveData<List<EventWithImage>>()

    fun getEvents(leagueId: String): LiveData<List<EventWithImage>> {
        eventScheduleList = mRepository.getLastMatchByLeagueId(leagueId)
        return eventScheduleList
    }
}
