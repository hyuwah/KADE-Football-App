package com.muhammadwahyudin.kadefootballapp.views.matchdetail

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.muhammadwahyudin.kadefootballapp.app.DB_OPS_STATE
import com.muhammadwahyudin.kadefootballapp.app.toReadableTimeWIB
import com.muhammadwahyudin.kadefootballapp.base.BaseViewModel
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.local.DatabaseHelper
import com.muhammadwahyudin.kadefootballapp.data.model.*
import io.reactivex.rxkotlin.addTo
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class MatchDetailViewModel(private val db: DatabaseHelper, private val repository: IRepository) : BaseViewModel() {

    private var state = MutableLiveData<ResourceState<EventWithImage>>(EmptyState())

    private var mEventWithImage = MutableLiveData<EventWithImage>()
    private var isFavorite = MutableLiveData(false)
    private var db_ops_state = MutableLiveData<DB_OPS_STATE>()

    fun loadMatchDetail(id: String): LiveData<ResourceState<EventWithImage>> {
        state.postValue(LoadingState())
        repository.getMatchDetail(id)
            .subscribe(
                { match ->
                    if (match != null) {
                        state.postValue(PopulatedState(match))
                        mEventWithImage = MutableLiveData(match)
                    } else state.postValue(NoResultState("No Result for ${id}"))
                },
                { t ->
                    state.postValue(ErrorState(t.localizedMessage))
                }
            )
            .addTo(compDisp)
        return state
    }

    private fun addToFavorite(matchId: String, match: EventWithImage) {
        try {
            db.use {
                insert(
                    FavoriteEvent.TABLE_NAME,
                    FavoriteEvent.EVENT_ID to matchId,
                    FavoriteEvent.EVENT_DATE to match.strTime?.toReadableTimeWIB(match.dateEvent!!),
                    FavoriteEvent.EVENT_NAME to match.strEvent,
                    FavoriteEvent.TEAM_HOME_NAME to match.strHomeTeam,
                    FavoriteEvent.TEAM_HOME_SCORE to match.intHomeScore,
                    FavoriteEvent.TEAM_HOME_BADGE_URL to match.strHomeTeamBadge,
                    FavoriteEvent.TEAM_AWAY_NAME to match.strAwayTeam,
                    FavoriteEvent.TEAM_AWAY_SCORE to match.intAwayScore,
                    FavoriteEvent.TEAM_AWAY_BADGE_URL to match.strAwayTeamBadge
                )
            }
            db_ops_state.postValue(DB_OPS_STATE.INSERT_SUCCESS)
        } catch (e: SQLiteConstraintException) {
            db_ops_state.postValue(DB_OPS_STATE.ERROR)
        }
    }

    private fun removeFromFavorite(matchId: String) {
        try {
            db.use {
                delete(
                    FavoriteEvent.TABLE_NAME,
                    "(EVENT_ID = {id})",
                    "id" to matchId
                )
            }
            db_ops_state.postValue(DB_OPS_STATE.REMOVE_SUCCESS)
        } catch (e: SQLiteConstraintException) {
            db_ops_state.postValue(DB_OPS_STATE.ERROR)
        }
    }

    fun updateFavoriteState(): LiveData<DB_OPS_STATE> {
        if (isFavorite.value!!) {
            removeFromFavorite(mEventWithImage.value!!.idEvent)
            isFavorite.postValue(false)
        } else {
            addToFavorite(mEventWithImage.value!!.idEvent, mEventWithImage.value!!)
            isFavorite.postValue(true)
        }
        return db_ops_state
    }

    fun isFavoritedMatch(matchId: String): LiveData<Boolean> {
        db.use {
            val result = select(FavoriteEvent.TABLE_NAME)
                .whereArgs("(EVENT_ID = {id})", "id" to matchId)
            val favorite = result.parseList(classParser<FavoriteEvent>())
            if (favorite.isNotEmpty()) {
                isFavorite.postValue(true)
            } else {
                isFavorite.postValue(false)
            }
        }
        return isFavorite
    }

}