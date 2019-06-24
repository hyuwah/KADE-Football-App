package com.muhammadwahyudin.kadefootballapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Team(
    var id: Long?,
    val idLeague: String? = "",
    val idSoccerXML: String? = "",
    val idTeam: String = "",
    val intFormedYear: String? = "",
    val intLoved: String? = "",
    val intStadiumCapacity: String? = "",
    val strAlternate: String? = "",
    val strCountry: String? = "",
    val strDescriptionCN: String? = "",
    val strDescriptionDE: String? = "",
    val strDescriptionEN: String? = "",
    val strDescriptionES: String? = "",
    val strDescriptionFR: String? = "",
    val strDescriptionHU: String? = "",
    val strDescriptionIL: String? = "",
    val strDescriptionIT: String? = "",
    val strDescriptionJP: String? = "",
    val strDescriptionNL: String? = "",
    val strDescriptionNO: String? = "",
    val strDescriptionPL: String? = "",
    val strDescriptionPT: String? = "",
    val strDescriptionRU: String? = "",
    val strDescriptionSE: String? = "",
    val strDivision: String? = "",
    val strFacebook: String? = "",
    val strGender: String? = "",
    val strInstagram: String? = "",
    val strKeywords: String? = "",
    val strLeague: String? = "",
    val strLocked: String? = "",
    val strManager: String? = "",
    val strRSS: String? = "",
    val strSport: String? = "",
    val strStadium: String? = "",
    val strStadiumDescription: String? = "",
    val strStadiumLocation: String? = "",
    val strStadiumThumb: String? = "",
    val strTeam: String? = "",
    val strTeamBadge: String = "",
    val strTeamBanner: String? = "",
    val strTeamFanart1: String? = "",
    val strTeamFanart2: String? = "",
    val strTeamFanart3: String? = "",
    val strTeamFanart4: String? = "",
    val strTeamJersey: String? = "",
    val strTeamLogo: String? = "",
    val strTeamShort: String? = "",
    val strTwitter: String? = "",
    val strWebsite: String? = "",
    val strYoutube: String? = ""
) : Parcelable {
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