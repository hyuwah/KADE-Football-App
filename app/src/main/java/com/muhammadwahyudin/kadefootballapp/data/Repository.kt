package com.muhammadwahyudin.kadefootballapp.data

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage
import com.muhammadwahyudin.kadefootballapp.data.model.League
import com.muhammadwahyudin.kadefootballapp.data.model.Team
import com.muhammadwahyudin.kadefootballapp.data.remote.TheSportDbApiService
import com.muhammadwahyudin.kadefootballapp.data.remote.response.LeagueDetailRes
import com.muhammadwahyudin.kadefootballapp.data.remote.response.TeamsRes
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers

class Repository(private val theSportDbApiService: TheSportDbApiService) {

    fun getLeagues(context: Context): List<League> {
        val leagues = arrayListOf<League>()
        val names = context.resources.getStringArray(R.array.league_name)
        val ids = context.resources.getIntArray(R.array.league_id)
        val desc = context.resources.getStringArray(R.array.league_desc)
        val images = context.resources.obtainTypedArray(R.array.league_image)
        for (i in names.indices) {
            leagues.add(League(ids[i], names[i], desc[i], images.getResourceId(i, 0)))
        }
        images.recycle()
        return leagues
    }

    fun getLeagueDetail(id: Int): MutableLiveData<LeagueDetailRes.League> {
        val data = MutableLiveData<LeagueDetailRes.League>()
        theSportDbApiService.getLeagueDetail(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.leagues[0]
            }
            .doOnSuccess {
                data.postValue(it)
            }.doOnError {
                data.postValue(null)
            }.subscribe()
        return data
    }

    fun getNextMatchByLeagueId(leagueId: String): MutableLiveData<List<EventWithImage>> {
        val tempdata = arrayListOf<EventWithImage>()
        val data = MutableLiveData<List<EventWithImage>>()
        theSportDbApiService.getNextMatchbyLeagueId(leagueId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                // Kumpulin Team Badge home & away
                if (!it.events.isNullOrEmpty()) {
                    it.events.forEach { event ->
                        Single.zip(
                            theSportDbApiService.getTeamDetail(event.idAwayTeam),
                            theSportDbApiService.getTeamDetail(event.idHomeTeam),
                            BiFunction<TeamsRes, TeamsRes, List<String>> { awayRes, homeRes ->
                                val pair = arrayListOf<String>()
                                pair.add(awayRes.teams[0].strTeamBadge)
                                pair.add(homeRes.teams[0].strTeamBadge)
                                pair.toList()
                            }
                        ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSuccess { teamBadge ->
                                event.strAwayTeamBadge = teamBadge[0]
                                event.strHomeTeamBadge = teamBadge[1]
                                tempdata.add(event)
                                data.postValue(tempdata.toList())
                            }.doOnError {
                                tempdata.add(event)
                                data.postValue(tempdata.toList())
                            }.subscribe()
                    }
                } else {
                    data.postValue(listOf())
                }
            }.doOnError {
                data.postValue(listOf())
            }.subscribe()
        return data
    }

    fun getLastMatchByLeagueId(leagueId: String): MutableLiveData<List<EventWithImage>> {
        val tempdata = arrayListOf<EventWithImage>()
        val data = MutableLiveData<List<EventWithImage>>()
        theSportDbApiService.getLastMatchbyLeagueId(leagueId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                // Kumpulin Team Badge home & away
                if (!it.events.isNullOrEmpty()) {
                    it.events.forEach { event ->
                        // https://medium.com/mindorks/how-to-make-complex-requests-simple-with-rxjava-in-kotlin-ccec004c5d10
                        Single.zip(
                            theSportDbApiService.getTeamDetail(event.idAwayTeam),
                            theSportDbApiService.getTeamDetail(event.idHomeTeam),
                            BiFunction<TeamsRes, TeamsRes, List<String>> { awayRes, homeRes ->
                                val pair = arrayListOf<String>()
                                pair.add(awayRes.teams[0].strTeamBadge)
                                pair.add(homeRes.teams[0].strTeamBadge)
                                pair.toList()
                            }
                        ).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnSuccess { teamBadge ->
                                event.strAwayTeamBadge = teamBadge[0]
                                event.strHomeTeamBadge = teamBadge[1]
                                tempdata.add(event)
                                data.postValue(tempdata.toList())
                            }.doOnError {
                                tempdata.add(event)
                                data.postValue(tempdata.toList())
                            }.subscribe()
                    }
                } else {
                    data.postValue(listOf())
                }
            }.doOnError {
                data.postValue(listOf())
            }.subscribe()
        return data
    }

    fun getTeamDetail(teamId: String): MutableLiveData<Team> {
        val data = MutableLiveData<Team>()
        theSportDbApiService.getTeamDetail(teamId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                data.postValue(it.teams[0])
            }.doOnError {
                data.postValue(null)
            }.subscribe()
        return data
    }
}