package com.muhammadwahyudin.kadefootballapp.views.matchdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.Repository
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage
import com.muhammadwahyudin.kadefootballapp.data.remote.TheSportDbApiService

class MatchDetailViewModel : ViewModel() {
    // TODO Dependency injection with Kodein / Koin
    private val mRepository = Repository(TheSportDbApiService.create())
    private var mEventWithImage = MutableLiveData<EventWithImage>()

    fun loadMatchDetail(id: String): LiveData<EventWithImage> {
        mEventWithImage = mRepository.getMatchDetail(id)
        return mEventWithImage
    }
}