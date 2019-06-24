package com.muhammadwahyudin.kadefootballapp.views.favorites.teams

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.local.DatabaseHelper
import com.muhammadwahyudin.kadefootballapp.data.model.Team

class FavoriteTeamViewModel(private val db: DatabaseHelper, val repository: IRepository) : ViewModel() {
    private var favoritedTeam = MutableLiveData<List<Team>>()

    fun loadFavoritedTeam(): LiveData<List<Team>> {
        favoritedTeam.postValue(repository.getFavoriteTeams(db))
        return favoritedTeam
    }
}