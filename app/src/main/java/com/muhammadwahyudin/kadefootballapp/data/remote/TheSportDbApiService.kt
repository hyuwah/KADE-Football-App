package com.muhammadwahyudin.kadefootballapp.data.remote

import com.muhammadwahyudin.kadefootballapp.BuildConfig
import com.muhammadwahyudin.kadefootballapp.data.remote.response.*
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TheSportDbApiService {

    // League Endpoint, strSport == soccer
    // https://www.thesportsdb.com/api/v1/json/1/all_leagues.php
    @GET("/api/v1/json/${BuildConfig.TSDB_API_KEY}/all_leagues.php")
    fun getLeagues(): Single<LeaguesRes>

    // League detail endpoint {leagueId}
    // https://www.thesportsdb.com/api/v1/json/1/lookupleague.php?id=4346
    @GET("/api/v1/json/${BuildConfig.TSDB_API_KEY}/lookupleague.php")
    fun getLeagueDetail(@Query("id") leagueId: Int): Single<LeagueDetailRes>

    // Match in League Endpoint {leagueId}
    // Next 15 https://www.thesportsdb.com/api/v1/json/1/eventsnextleague.php?id=4328
    @GET("/api/v1/json/${BuildConfig.TSDB_API_KEY}/eventsnextleague.php")
    fun getNextMatchByLeagueId(@Query("id") leagueId: String): Single<EventsRes>
    // Last 15 https://www.thesportsdb.com/api/v1/json/1/eventspastleague.php?id=4328
    @GET("/api/v1/json/${BuildConfig.TSDB_API_KEY}/eventspastleague.php")
    fun getLastMatchByLeagueId(@Query("id") leagueId: String): Single<EventsRes>

    // Detail Match endpoint {eventId}
    // https://www.thesportsdb.com/api/v1/json/1/lookupevent.php?id=441613
    // Kontennya kok sama kaya next15 / last15 ??
    @GET("/api/v1/json/${BuildConfig.TSDB_API_KEY}/lookupevent.php")
    fun getMatchDetailByEventId(@Query("id") eventId: String): Single<EventsRes>

    // Search match endpoint {stringQuery}
    // https://www.thesportsdb.com/api/v1/json/1/searchevents.php?e=Arsenal_vs_Chelsea
    @GET("/api/v1/json/${BuildConfig.TSDB_API_KEY}/searchevents.php")
    fun searchMatches(@Query("e") query: String): Single<SearchEventsRes>

    // Team Detail endpoint {teamId}
    // https://www.thesportsdb.com/api/v1/json/1/lookupteam.php?id=133604
    @GET("/api/v1/json/${BuildConfig.TSDB_API_KEY}/lookupteam.php")
    fun getTeamDetail(@Query("id") teamId: String): Single<TeamsRes>

    companion object Factory {
        fun create(): TheSportDbApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BuildConfig.BASE_URL) // TODO-ASK base url sampe domain .com ato sampe common path terakhir (apikey) ?
                .build()

            return retrofit.create(TheSportDbApiService::class.java)
        }
    }
}