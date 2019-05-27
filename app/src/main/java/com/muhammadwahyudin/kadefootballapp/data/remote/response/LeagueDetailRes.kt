package com.muhammadwahyudin.kadefootballapp.data.remote.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class LeagueDetailRes(
    val leagues: List<League>
) {
    @Parcelize
    data class League(
        val dateFirstEvent: String = "",
        val idCup: String = "",
        val idLeague: String = "",
        val idSoccerXML: String = "",
        val intFormedYear: String = "",
        val strBadge: String = "",
        val strBanner: String = "",
        val strComplete: String = "",
        val strCountry: String = "",
        val strDescriptionCN: String? = "",
        val strDescriptionDE: String? = "",
        val strDescriptionEN: String = "",
        val strDescriptionES: String? = "",
        val strDescriptionFR: String = "",
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
        val strDivision: String = "",
        val strFacebook: String = "",
        val strFanart1: String = "",
        val strFanart2: String = "",
        val strFanart3: String = "",
        val strFanart4: String = "",
        val strGender: String = "",
        val strLeague: String = "",
        val strLeagueAlternate: String = "",
        val strLocked: String = "",
        val strLogo: String = "",
        val strNaming: String = "",
        val strPoster: String = "",
        val strRSS: String = "",
        val strSport: String = "",
        val strTrophy: String = "",
        val strTwitter: String = "",
        val strWebsite: String = "",
        val strYoutube: String = ""
    ) : Parcelable
}