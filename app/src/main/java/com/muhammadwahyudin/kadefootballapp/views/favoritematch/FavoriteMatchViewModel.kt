package com.muhammadwahyudin.kadefootballapp.views.favoritematch

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.local.database
import com.muhammadwahyudin.kadefootballapp.data.model.FavoriteEvent
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class FavoriteMatchViewModel : ViewModel() {
    // TODO Ambil dari DI / Kodein
    private var favoritedEventsSchedule = MutableLiveData<List<FavoriteEvent>>()

    fun loadFavoritedEvents(context: Context): LiveData<List<FavoriteEvent>> {
        context.database.use {
            val result = select(FavoriteEvent.TABLE_NAME)
            val favorite = result.parseList(classParser<FavoriteEvent>())
            favoritedEventsSchedule.postValue(favorite)
        }
        return favoritedEventsSchedule
    }

}