package com.muhammadwahyudin.kadefootballapp.views.playerdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.model.*

class PlayerDetailViewModel(private val repo: IRepository) : ViewModel() {
    private val state = MutableLiveData<ResourceState<Player>>(EmptyState())

    fun getPlayerDetail(playerId: String): LiveData<ResourceState<Player>> {
        state.postValue(LoadingState())
        return Transformations.switchMap(repo.getPlayerDetail(playerId)) {
            if (it != null)
                state.postValue(PopulatedState(it))
            else
                state.postValue(NoResultState("No Result for $playerId"))
            state
        }
    }
}