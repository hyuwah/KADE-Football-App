package com.muhammadwahyudin.kadefootballapp.views.favorites.match

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.local.DatabaseHelper
import com.muhammadwahyudin.kadefootballapp.data.model.FavoriteEvent

class FavoriteMatchViewModel(private val db: DatabaseHelper, val repository: IRepository) : ViewModel() {
    private var favoritedEventsSchedule = MutableLiveData<List<FavoriteEvent>>()

    fun loadFavoritedEvents(): LiveData<List<FavoriteEvent>> {
        favoritedEventsSchedule.postValue(repository.getFavoriteEvents(db))
        return favoritedEventsSchedule
    }

}