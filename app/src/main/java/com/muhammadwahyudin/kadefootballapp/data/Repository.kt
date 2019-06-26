package com.muhammadwahyudin.kadefootballapp.data

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.data.local.DatabaseHelper
import com.muhammadwahyudin.kadefootballapp.data.model.*
import com.muhammadwahyudin.kadefootballapp.data.remote.TheSportDbApiService
import com.muhammadwahyudin.kadefootballapp.data.remote.response.EventsRes
import com.muhammadwahyudin.kadefootballapp.data.remote.response.LeagueDetailRes
import com.muhammadwahyudin.kadefootballapp.data.remote.response.SearchEventsRes
import com.muhammadwahyudin.kadefootballapp.data.remote.response.TeamsRes
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class Repository(
    private val bgScheduler: Scheduler,
    private val mainScheduler: Scheduler,
    private val theSportDbApiService: TheSportDbApiService
) : IRepository {

    private val TAG = Repository::class.java.simpleName

    override fun getLeagues(resources: Resources): List<League> {
        val leagues = arrayListOf<League>()
        val names = resources.getStringArray(R.array.league_name)
        val ids = resources.getIntArray(R.array.league_id)
        val desc = resources.getStringArray(R.array.league_desc)
        val images = resources.obtainTypedArray(R.array.league_image)
        for (i in names.indices) {
            leagues.add(League(ids[i], names[i], desc[i], images.getResourceId(i, 0)))
        }
        images.recycle()
        return leagues
    }

    override fun getLeagueDetail(id: Int): MutableLiveData<LeagueDetailRes.League> {
        val data = MutableLiveData<LeagueDetailRes.League>()
        theSportDbApiService.getLeagueDetail(id)
            .subscribeOn(bgScheduler)
            .observeOn(mainScheduler)
            .map {
                if (!it.leagues.isNullOrEmpty())
                    it.leagues[0]
                else
                    LeagueDetailRes.League() // Return Empty LeagueDetail data if no data from network
            }
            .doOnSuccess {
                data.postValue(it)
            }.doOnError {
                Log.e(TAG, "Network exception for league id: $id", it)
            }.subscribe()
        return data
    }

    override fun getNextMatchByLeagueId(leagueId: String): MutableLiveData<List<EventWithImage>> {
        val tempData = arrayListOf<EventWithImage>()
        val data = MutableLiveData<List<EventWithImage>>()
        theSportDbApiService.getNextMatchByLeagueId(leagueId)
            .subscribeOn(bgScheduler)
            .observeOn(mainScheduler)
            .doOnSuccess {
                if (!it.events.isNullOrEmpty()) updateEventsWithTeamBadge(it, tempData, data)
                else data.postValue(listOf())
            }.doOnError {
                data.postValue(listOf())
            }.subscribe()
        return data
    }

    override fun getLastMatchByLeagueId(leagueId: String): MutableLiveData<List<EventWithImage>> {
        val tempData = arrayListOf<EventWithImage>()
        val data = MutableLiveData<List<EventWithImage>>()
        theSportDbApiService.getLastMatchByLeagueId(leagueId)
            .subscribeOn(bgScheduler)
            .observeOn(mainScheduler)
            .doOnSuccess {
                if (!it.events.isNullOrEmpty()) updateEventsWithTeamBadge(it, tempData, data)
                else data.postValue(listOf())
            }.doOnError {
                data.postValue(listOf())
            }.subscribe()
        return data
    }

    override fun searchMatches(query: String): Single<SearchEventsRes> {
        return theSportDbApiService.searchMatches(query)
    }

    override fun searchTeams(query: String): Single<TeamsRes> {
        return theSportDbApiService.searchTeams(query)
    }

    override fun getMatchDetail(eventId: String): MutableLiveData<EventWithImage> {
        val data = MutableLiveData<EventWithImage>()
        theSportDbApiService.getMatchDetailByEventId(eventId)
            .subscribeOn(bgScheduler)
            .observeOn(mainScheduler)
            .doOnSuccess {
                if (!it.events.isNullOrEmpty()) data.postValue(it.events[0])
            }.doOnError {
                Log.e(TAG, "Network exception for event id: $eventId", it)
            }.subscribe()
        return data
    }

    override fun getTeamDetail(teamId: String): MutableLiveData<Team> {
        val data = MutableLiveData<Team>()
        theSportDbApiService.getTeamDetail(teamId)
            .subscribeOn(bgScheduler)
            .observeOn(mainScheduler)
            .doOnSuccess {
                if (!it.teams.isNullOrEmpty()) data.postValue(it.teams[0])
            }.doOnError {
                Log.e(TAG, "Network exception for team id: $teamId", it)
            }.subscribe()
        return data
    }

    override fun getFavoriteEvents(db: DatabaseHelper): List<FavoriteEvent> {
        return db.use {
            val result = select(FavoriteEvent.TABLE_NAME)
            return@use result.parseList(classParser())
        }
    }

    override fun updateEventWithTeamBadge(event: EventWithImage): Single<List<String>> {
        return Single.zip(
            theSportDbApiService.getTeamDetail(event.idAwayTeam),
            theSportDbApiService.getTeamDetail(event.idHomeTeam),
            BiFunction<TeamsRes, TeamsRes, List<String>> { awayRes, homeRes ->
                val teamBadges = arrayListOf<String>()
                teamBadges.add(awayRes.teams[0].strTeamBadge)
                teamBadges.add(homeRes.teams[0].strTeamBadge)
                teamBadges.toList()
            }
        )
    }

    private fun updateEventsWithTeamBadge(
        it: EventsRes, // Cuman beda serialized name doang v. EventWithImage, bad api response design
        tempData: ArrayList<EventWithImage>,
        data: MutableLiveData<List<EventWithImage>>
    ) {
        // Kumpulin Team Badge home & away
        it.events.forEach { event ->
            // https://medium.com/mindorks/how-to-make-complex-requests-simple-with-rxjava-in-kotlin-ccec004c5d10
            Single.zip(
                theSportDbApiService.getTeamDetail(event.idAwayTeam),
                theSportDbApiService.getTeamDetail(event.idHomeTeam),
                BiFunction<TeamsRes, TeamsRes, List<String>> { awayRes, homeRes ->
                    val teamBadges = arrayListOf<String>()
                    teamBadges.add(awayRes.teams[0].strTeamBadge)
                    teamBadges.add(homeRes.teams[0].strTeamBadge)
                    teamBadges.toList()
                })
                .subscribeOn(bgScheduler)
                .observeOn(mainScheduler)
                .doOnSuccess { teamBadges ->
                    // insert team badges ke masing-masing team di event
                    event.strAwayTeamBadge = teamBadges[0]
                    event.strHomeTeamBadge = teamBadges[1]
                    tempData.add(event)
                    data.postValue(tempData.toList())
                }.doOnError {
                    tempData.add(event)
                    data.postValue(tempData.toList())
                    Log.e(TAG, "Network exception for event id: ${event.idEvent}", it)
                }.subscribe()
        }
    }

    override fun getTeamList(leagueId: String): MutableLiveData<List<Team>> {
        val data = MutableLiveData<List<Team>>()
        theSportDbApiService.getTeamListByLeagueId(leagueId)
            .subscribeOn(bgScheduler)
            .observeOn(mainScheduler)
            .doOnSuccess {
                if (!it.teams.isNullOrEmpty())
                    data.postValue(it.teams)
            }
            .doOnError {

            }
            .subscribe()
        return data
    }

    override fun getFavoriteTeams(db: DatabaseHelper): List<Team> {
        return db.use {
            val result = select(Team.TABLE_NAME)
            return@use result.parseList(classParser())
        }
    }

    override fun getPlayerList(teamId: String): MutableLiveData<List<Player>> {
        val data = MutableLiveData<List<Player>>()
        theSportDbApiService.getPlayerList(teamId)
            .subscribeOn(bgScheduler)
            .observeOn(mainScheduler)
            .doOnSuccess {
                if (!it.player.isNullOrEmpty())
                    data.postValue(it.player)
            }
            .doOnError {

            }
            .subscribe()
        return data
    }

    override fun getPlayerDetail(playerId: String): MutableLiveData<Player> {
        val data = MutableLiveData<Player>()
        theSportDbApiService.getPlayerDetail(playerId)
            .subscribeOn(bgScheduler)
            .observeOn(mainScheduler)
            .doOnSuccess {
                if (!it.players.isNullOrEmpty())
                    data.postValue(it.players[0])
            }
            .doOnError {

            }
            .subscribe()
        return data
    }

    override fun getLeagueStandings(leagueId: String): MutableLiveData<List<Standing>> {
        val data = MutableLiveData<List<Standing>>()
        theSportDbApiService.getLeagueStandings(leagueId)
            .subscribeOn(bgScheduler)
            .observeOn(mainScheduler)
            .doOnSuccess {
                if (!it.table.isNullOrEmpty())
                    data.postValue(it.table)
            }
            .doOnError {

            }
            .subscribe()
        return data
    }

}