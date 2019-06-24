package com.muhammadwahyudin.kadefootballapp.views.teamdetail

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muhammadwahyudin.kadefootballapp.app.DB_OPS_STATE
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.data.local.DatabaseHelper
import com.muhammadwahyudin.kadefootballapp.data.model.Team
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class TeamDetailViewModel(private val db: DatabaseHelper, private val repository: IRepository) : ViewModel() {

    private var isFavorite = MutableLiveData(false)
    private var db_ops_state = MutableLiveData<DB_OPS_STATE>()

    private fun addToFavorite(teamId: String, team: Team) {
        try {
            db.use {
                insert(
                    Team.TABLE_NAME,
                    Team.ID_LEAGUE to team.idLeague,
                    Team.ID_SOCCER_XML to team.idSoccerXML,
                    Team.ID_TEAM to team.idTeam,
                    Team.FORMED_YEAR to team.intFormedYear,
                    Team.STADIUM_CAPACITY to team.intStadiumCapacity,
                    Team.STR_ALTERNATE to team.strAlternate,
                    Team.STR_COUNTRY to team.strCountry,
                    Team.DESC_EN to team.strDescriptionEN,
                    Team.STR_LEAGUE to team.strLeague,
                    Team.STR_STADIUM to team.strStadium,
                    Team.STR_STADIUM_LOC to team.strStadiumLocation,
                    Team.STR_TEAM to team.strTeam,
                    Team.TEAM_BADGE_URL to team.strTeamBadge,
                    Team.TEAM_BANNER to team.strTeamBanner,
                    Team.TEAM_FANART_1 to team.strTeamFanart1,
                    Team.TEAM_FANART_2 to team.strTeamFanart2,
                    Team.TEAM_FANART_3 to team.strTeamFanart3,
                    Team.TEAM_FANART_4 to team.strTeamFanart4,
                    Team.TEAM_JERSEY to team.strTeamJersey
                )
            }
            db_ops_state.postValue(DB_OPS_STATE.INSERT_SUCCESS)
        } catch (e: SQLiteConstraintException) {
            db_ops_state.postValue(DB_OPS_STATE.ERROR)
        }
    }

    private fun removeFromFavorite(teamId: String) {
        try {
            db.use {
                delete(
                    Team.TABLE_NAME,
                    "(${Team.ID_TEAM}= {id})",
                    "id" to teamId
                )
            }
            db_ops_state.postValue(DB_OPS_STATE.REMOVE_SUCCESS)
        } catch (e: SQLiteConstraintException) {
            db_ops_state.postValue(DB_OPS_STATE.ERROR)
        }
    }

    fun updateFavoriteState(team: Team): LiveData<DB_OPS_STATE> {
        if (isFavorite.value!!) {
            removeFromFavorite(team.idTeam)
            isFavorite.postValue(false)
        } else {
            addToFavorite(team.idTeam, team)
            isFavorite.postValue(true)
        }
        return db_ops_state
    }

    fun isFavoritedTeam(teamId: String): LiveData<Boolean> {
        db.use {
            val result = select(Team.TABLE_NAME).whereArgs("(${Team.ID_TEAM} = {id})", "id" to teamId)
            val favorite = result.parseList(classParser<Team>())
            if (favorite.isNotEmpty())
                isFavorite.postValue(true)
            else
                isFavorite.postValue(false)
        }
        return isFavorite
    }
}