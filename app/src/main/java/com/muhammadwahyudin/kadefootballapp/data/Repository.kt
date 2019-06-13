package com.muhammadwahyudin.kadefootballapp.data

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.data.local.database
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage
import com.muhammadwahyudin.kadefootballapp.data.model.FavoriteEvent
import com.muhammadwahyudin.kadefootballapp.data.model.League
import com.muhammadwahyudin.kadefootballapp.data.model.Team
import com.muhammadwahyudin.kadefootballapp.data.remote.TheSportDbApiService
import com.muhammadwahyudin.kadefootballapp.data.remote.response.EventsRes
import com.muhammadwahyudin.kadefootballapp.data.remote.response.LeagueDetailRes
import com.muhammadwahyudin.kadefootballapp.data.remote.response.SearchEventsRes
import com.muhammadwahyudin.kadefootballapp.data.remote.response.TeamsRes
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select

class Repository(private val theSportDbApiService: TheSportDbApiService) : IRepository {

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
        theSportDbApiService.getLeagueDetail(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.leagues[0]
            }
            .doOnSuccess {
                data.postValue(it)
            }.doOnError {

            }.subscribe()
        return data
    }

    override fun getNextMatchByLeagueId(leagueId: String): MutableLiveData<List<EventWithImage>> {
        val tempData = arrayListOf<EventWithImage>()
        val data = MutableLiveData<List<EventWithImage>>()
        theSportDbApiService.getNextMatchByLeagueId(leagueId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
        theSportDbApiService.getLastMatchByLeagueId(leagueId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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

    override fun getMatchDetail(eventId: String): MutableLiveData<EventWithImage> {
        val data = MutableLiveData<EventWithImage>()
        theSportDbApiService.getMatchDetailByEventId(eventId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                if (!it.events.isNullOrEmpty()) data.postValue(it.events[0])
            }.doOnError {

            }.subscribe()
        return data
    }

    override fun getTeamDetail(teamId: String): MutableLiveData<Team> {
        val data = MutableLiveData<Team>()
        theSportDbApiService.getTeamDetail(teamId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                data.postValue(it.teams[0])
            }.doOnError {

            }.subscribe()
        return data
    }

    override fun getFavoriteEvents(context: Context): List<FavoriteEvent> {
        return context.database.use {
            val result = select(FavoriteEvent.TABLE_NAME)
            val favorite = result.parseList(classParser<FavoriteEvent>())
            return@use favorite
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
        it: EventsRes, // Cuman beda serialized name doang, bad api response
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
                }
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSuccess { teamBadges ->
                    // insert team badges ke masing-masing team di event
                    event.strAwayTeamBadge = teamBadges[0]
                    event.strHomeTeamBadge = teamBadges[1]
                    tempData.add(event)
                    data.postValue(tempData.toList())
                }.doOnError {
                    tempData.add(event)
                    data.postValue(tempData.toList())
                }.subscribe()
        }
    }
}