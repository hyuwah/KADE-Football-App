package com.muhammadwahyudin.kadefootballapp.views.matchdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage

class MatchDetailViewModel(private val repository: IRepository) : ViewModel() {
    private var mEventWithImage = MutableLiveData<EventWithImage>()

    fun loadMatchDetail(id: String): LiveData<EventWithImage> {
        mEventWithImage = repository.getMatchDetail(id)
        return mEventWithImage
    }
}