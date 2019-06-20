package com.muhammadwahyudin.kadefootballapp.data

import android.content.res.Resources
import androidx.lifecycle.MutableLiveData
import com.muhammadwahyudin.kadefootballapp.data.local.DatabaseHelper
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage
import com.muhammadwahyudin.kadefootballapp.data.model.FavoriteEvent
import com.muhammadwahyudin.kadefootballapp.data.model.League
import com.muhammadwahyudin.kadefootballapp.data.model.Team
import com.muhammadwahyudin.kadefootballapp.data.remote.response.LeagueDetailRes
import com.muhammadwahyudin.kadefootballapp.data.remote.response.SearchEventsRes
import io.reactivex.Single

interface IRepository {
    fun getLeagues(resources: Resources): List<League>
    fun getLeagueDetail(id: Int): MutableLiveData<LeagueDetailRes.League>
    fun getNextMatchByLeagueId(leagueId: String): MutableLiveData<List<EventWithImage>>
    fun getLastMatchByLeagueId(leagueId: String): MutableLiveData<List<EventWithImage>>
    fun searchMatches(query: String): Single<SearchEventsRes>
    fun getMatchDetail(eventId: String): MutableLiveData<EventWithImage>
    fun getTeamDetail(teamId: String): MutableLiveData<Team>
    fun getFavoriteEvents(db: DatabaseHelper): List<FavoriteEvent>
    fun updateEventWithTeamBadge(event: EventWithImage): Single<List<String>>
    fun getTeamList(leagueId: String): MutableLiveData<List<Team>>
}