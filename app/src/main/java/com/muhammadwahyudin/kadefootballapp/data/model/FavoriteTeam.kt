package com.muhammadwahyudin.kadefootballapp.data.model

data class FavoriteTeam(
    val id: Long?,
    val idTeam: String?,
    val idLeague: String?,
    val strTeam: String?,
    val strTeamBadge: String?,
    val intFormedYear: String?,
    val strAlternate: String?,
    val strCountry: String?,
    val strLeague: String?,
    val strStadium: String?,
    val strStadiumLocation: String?
) {
    companion object {
        const val TABLE_NAME = "TABLE_FAVORITE_TEAM"
        const val ID = "ID"
        const val ID_TEAM = "ID_TEAM"
        const val ID_LEAGUE = "ID_LEAGUE"
        const val STR_TEAM = "STR_TEAM"
        const val TEAM_BADGE_URL = "TEAM_BADGE_URL"
        const val FORMED_YEAR = "FORMED_YEAR"
        const val STR_ALTERNATE = "STR_ALTERNATE"
        const val STR_COUNTRY = "STR_COUNTRY"
        const val STR_LEAGUE = "STR_LEAGUE"
        const val STR_STADIUM = "STR_STADIUM"
        const val STR_STADIUM_LOC = "STR_STADIUM_LOC"
    }
}