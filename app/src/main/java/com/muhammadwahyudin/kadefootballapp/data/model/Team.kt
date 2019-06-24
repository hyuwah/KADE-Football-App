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
        const val ID_LEAGUE = "ID_LEAGUE"
        const val ID_SOCCER_XML = "ID_SOCCER_XML"
        const val ID_TEAM = "ID_TEAM"
        const val FORMED_YEAR = "FORMED_YEAR"
        const val LOVED = "LOVED"
        const val STADIUM_CAPACITY = "STADIUM_CAPACITY"
        const val STR_ALTERNATE = "STR_ALTERNATE"
        const val STR_COUNTRY = "STR_COUNTRY"
        const val DESC_CN = "DESC_CN"
        const val DESC_DE = "DESC_DE"
        const val DESC_EN = "DESC_EN"
        const val DESC_ES = "DESC_ES"
        const val DESC_FR = "DESC_FR"
        const val DESC_HU = "DESC_HU"
        const val DESC_IL = "DESC_IL"
        const val DESC_IT = "DESC_IT"
        const val DESC_JP = "DESC_JP"
        const val DESC_NL = "DESC_NL"
        const val DESC_NO = "DESC_NO"
        const val DESC_PL = "DESC_PL"
        const val DESC_PT = "DESC_PT"
        const val DESC_RU = "DESC_RU"
        const val DESC_SE = "DESC_SE"
        const val DIVISION = "DIVISION"
        const val FACEBOOK = "FACEBOOK"
        const val GENDER = "GENDER"
        const val INSTAGRAM = "INSTAGRAM"
        const val KEYWORDS = "KEYWORDS"
        const val STR_LEAGUE = "STR_LEAGUE"
        const val LOCKED = "LOCKED"
        const val MANAGER = "MANAGER"
        const val RSS = "RSS"
        const val SPORT = "SPORT"
        const val STR_STADIUM = "STR_STADIUM"
        const val STR_STADIUM_DESC = "STR_STADIUM_DESC"
        const val STR_STADIUM_LOC = "STR_STADIUM_LOC"
        const val STR_STADIUM_THUMB = "STR_STADIUM_THUMB"
        const val STR_TEAM = "STR_TEAM"
        const val TEAM_BADGE_URL = "TEAM_BADGE_URL"
        const val TEAM_BANNER = "TEAM_BANNER"
        const val TEAM_FANART_1 = "TEAM_FANART_1"
        const val TEAM_FANART_2 = "TEAM_FANART_2"
        const val TEAM_FANART_3 = "TEAM_FANART_3"
        const val TEAM_FANART_4 = "TEAM_FANART_4"
        const val TEAM_JERSEY = "TEAM_JERSEY"
        const val TEAM_LOGO = "TEAM_LOGO"
        const val TEAM_SHORT = "TEAM_SHORT"
        const val TWITTER = "TWITTER"
        const val WEBSITE = "WEBSITE"
        const val YOUTUBE = "YOUTUBE"
    }
}