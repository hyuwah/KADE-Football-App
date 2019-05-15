package com.muhammadwahyudin.kadefootballapp.data

import android.content.Context
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.data.model.League
import com.muhammadwahyudin.kadefootballapp.data.remote.TheSportDbApiService
import com.muhammadwahyudin.kadefootballapp.data.remote.response.LeagueDetailRes
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
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

    fun getLeagueDetail(id: Int): Single<LeagueDetailRes.League> {
        return theSportDbApiService.getLeagueDetail(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.leagues[0]
            }
    }
}