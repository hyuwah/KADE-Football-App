package com.muhammadwahyudin.kadefootballapp.views.teamdetail.playerlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.model.*

class PlayerListViewModel(private val repository: IRepository) : ViewModel() {
    private val state = MutableLiveData<ResourceState<List<Player>>>(EmptyState())

    fun getPlayerList(teamId: String): LiveData<ResourceState<List<Player>>> {
        state.postValue(LoadingState())
        return Transformations.switchMap(repository.getPlayerList(teamId)) {
            if (it.isNullOrEmpty())
                state.postValue(NoResultState("No player found"))
            else
                state.postValue(PopulatedState(it))
            state
        }
    }
}