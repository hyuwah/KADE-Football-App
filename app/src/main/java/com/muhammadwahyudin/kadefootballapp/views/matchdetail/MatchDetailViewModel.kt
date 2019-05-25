package com.muhammadwahyudin.kadefootballapp.views.matchdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.Repository
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage

class MatchDetailViewModel(private val repository: Repository) : ViewModel() {
    private var mEventWithImage = MutableLiveData<EventWithImage>()

    fun loadMatchDetail(id: String): LiveData<EventWithImage> {
        mEventWithImage = repository.getMatchDetail(id)
        return mEventWithImage
    }
}