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
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import retrofit2.Response

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

    override fun getLeagueDetail(id: Int): Single<LeagueDetailRes.League> {
        return theSportDbApiService.getLeagueDetail(id)
            .subscribeOn(bgScheduler)
            .observeOn(mainScheduler)
            .map {
                if (it.body()?.leagues != null && !it.body()?.leagues.isNullOrEmpty())
                    it.body()!!.leagues[0]
                else
                    LeagueDetailRes.League() // Return Empty LeagueDetail data if no data from network
            }
    }

    override fun getNextMatchByLeagueId(leagueId: String): Single<List<EventWithImage>> {
        return theSportDbApiService.getNextMatchByLeagueId(leagueId)
            .subscribeOn(bgScheduler)
            .observeOn(mainScheduler)
            .flatMap {
                if (it.body()?.events != null) {
                    Single.just(it)
                        .map { response ->
                            response.body()?.events
                        }
                        .flattenAsObservable { t -> t }
                        .flatMapSingle { match ->
                            updateEventWithTeamBadge(match)
                                .flatMap { t ->
                                    match.strAwayTeamBadge = t[0]
                                    match.strHomeTeamBadge = t[1]
                                    Single.just(match)
                                }
                                .subscribeOn(Schedulers.io())
                        }
                        .toList()
                } else {
                    Single.just(emptyList())
                }
            }
    }

    override fun getLastMatchByLeagueId(leagueId: String): Single<List<EventWithImage>> {
        return theSportDbApiService.getLastMatchByLeagueId(leagueId)
            .subscribeOn(bgScheduler)
            .observeOn(mainScheduler)
            .flatMap {
                if (it.body()?.events != null) {
                    Single.just(it)
                        .map { response ->
                            response.body()?.events
                        }
                        .flattenAsObservable { t -> t }
                        .flatMapSingle { match ->
                            updateEventWithTeamBadge(match)
                                .flatMap { t ->
                                    match.strAwayTeamBadge = t[0]
                                    match.strHomeTeamBadge = t[1]
                                    Single.just(match)
                                }
                                .subscribeOn(Schedulers.io())
                        }
                        .toList()
                } else {
                    Single.just(emptyList())
                }
            }
    }

    override fun searchMatches(query: String): Single<SearchEventsRes> {
        return theSportDbApiService.searchMatches(query)
    }

    override fun searchTeams(query: String): Single<TeamsRes> {
        return theSportDbApiService.searchTeams(query)
    }

    override fun getMatchDetail(eventId: String): Single<EventWithImage?> {
        return theSportDbApiService.getMatchDetailByEventId(eventId)
            .subscribeOn(bgScheduler)
            .observeOn(mainScheduler)
            .map {
                if (it.body() != null && it.body()?.events!!.isNotEmpty()) it.body()!!.events?.get(0) else null
            }
    }

    override fun getTeamDetail(teamId: String): Single<Team> {
        return theSportDbApiService.getTeamDetail(teamId)
            .subscribeOn(bgScheduler)
            .observeOn(mainScheduler)
            .map {
                if (it.body() != null) it.body()!!.teams[0]
                else null
            }
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
            BiFunction<Response<TeamsRes>, Response<TeamsRes>, List<String>> { awayRes, homeRes ->
                val teamBadges = arrayListOf<String>()
                teamBadges.add(awayRes.body()!!.teams[0].strTeamBadge)
                teamBadges.add(homeRes.body()!!.teams[0].strTeamBadge)
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
        it.events?.forEach { event ->
            // https://medium.com/mindorks/how-to-make-complex-requests-simple-with-rxjava-in-kotlin-ccec004c5d10
            Single.zip(
                theSportDbApiService.getTeamDetail(event.idAwayTeam),
                theSportDbApiService.getTeamDetail(event.idHomeTeam),
                BiFunction<Response<TeamsRes>, Response<TeamsRes>, List<String>> { awayRes, homeRes ->
                    val teamBadges = arrayListOf<String>()
                    teamBadges.add(awayRes.body()!!.teams[0].strTeamBadge)
                    teamBadges.add(homeRes.body()!!.teams[0].strTeamBadge)
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

    override fun getTeamList(leagueId: String): Single<List<Team>> {
        return theSportDbApiService.getTeamListByLeagueId(leagueId)
            .subscribeOn(bgScheduler)
            .observeOn(mainScheduler)
            .map {
                if (it.body() != null) it.body()!!.teams
                else emptyList()
            }
    }

    override fun getFavoriteTeams(db: DatabaseHelper): List<Team> {
        return db.use {
            val result = select(Team.TABLE_NAME)
            return@use result.parseList(classParser())
        }
    }

    override fun getPlayerList(teamId: String): Single<List<Player>> {
        return theSportDbApiService.getPlayerList(teamId)
            .subscribeOn(bgScheduler)
            .observeOn(mainScheduler)
            .map {
                if (it.body() != null) it.body()!!.player
                else emptyList()
            }
    }

    override fun getPlayerDetail(playerId: String): Single<Player> {
        return theSportDbApiService.getPlayerDetail(playerId)
            .subscribeOn(bgScheduler)
            .observeOn(mainScheduler)
            .map {
                it.body()!!.players[0]
            }

    }

    override fun getLeagueStandings(leagueId: String): Single<List<Standing>> {
        return theSportDbApiService.getLeagueStandings(leagueId)
            .subscribeOn(bgScheduler)
            .observeOn(mainScheduler)
            .map {
                if (it.body()?.table != null && !it.body()?.table.isNullOrEmpty())
                    it.body()!!.table
                else
                    emptyList()
            }
    }

}