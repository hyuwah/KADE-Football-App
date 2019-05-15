package com.muhammadwahyudin.kadefootballapp.data.remote

import com.muhammadwahyudin.kadefootballapp.data.remote.response.LeagueDetailRes
import com.muhammadwahyudin.kadefootballapp.data.remote.response.LeaguesRes
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TheSportDbApiService {

    // League Endpoint, strSport == soccer
    // https://www.thesportsdb.com/api/v1/json/1/all_leagues.php
    @GET("/api/v1/json/1/all_leagues.php")
    fun getLeagues(): Single<LeaguesRes>

    // League detail endpoint {leagueId}
    // https://www.thesportsdb.com/api/v1/json/1/lookupleague.php?id=4346
    // add /preview for thumbnail image
    @GET("/api/v1/json/1/lookupleague.php")
    fun getLeagueDetail(@Query("id") leagueId: Int): Single<LeagueDetailRes>

    // Match in League Endpoint {leagueId}
    // Next 15 https://www.thesportsdb.com/api/v1/json/1/eventsnextleague.php?id=4328
    // Last 15 https://www.thesportsdb.com/api/v1/json/1/eventspastleague.php?id=4328

    // Detail Match endpoint {eventId}
    // https://www.thesportsdb.com/api/v1/json/1/lookupevent.php?id=441613

    // Search match endpoint {stringQuery}
    // https://www.thesportsdb.com/api/v1/json/1/searchevents.php?e=Arsenal_vs_Chelsea

    companion object Factory {
        fun create(): TheSportDbApiService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://www.thesportsdb.com") // TODO-ASK base url sampe domain .com ato sampe common path terakhir (apikey) ?
                .build()

            return retrofit.create(TheSportDbApiService::class.java)
        }
    }
}