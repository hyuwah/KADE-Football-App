package com.muhammadwahyudin.kadefootballapp.data.remote.response

data class LeaguesRes(
    val leagues: List<League>
) {

    data class League(
        val idLeague: String,
        val strLeague: String,
        val strLeagueAlternate: Any,
        val strSport: String
    )
}