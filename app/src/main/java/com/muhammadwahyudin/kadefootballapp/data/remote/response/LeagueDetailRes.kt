package com.muhammadwahyudin.kadefootballapp.data.remote.response

data class LeagueDetailRes(
    val leagues: List<League>
) {
    data class League(
        val dateFirstEvent: String,
        val idCup: String,
        val idLeague: String,
        val idSoccerXML: String,
        val intFormedYear: String,
        val strBadge: String,
        val strBanner: String,
        val strComplete: String,
        val strCountry: String,
        val strDescriptionCN: Any,
        val strDescriptionDE: Any,
        val strDescriptionEN: String,
        val strDescriptionES: Any,
        val strDescriptionFR: String,
        val strDescriptionHU: Any,
        val strDescriptionIL: Any,
        val strDescriptionIT: Any,
        val strDescriptionJP: Any,
        val strDescriptionNL: Any,
        val strDescriptionNO: Any,
        val strDescriptionPL: Any,
        val strDescriptionPT: Any,
        val strDescriptionRU: Any,
        val strDescriptionSE: Any,
        val strDivision: String,
        val strFacebook: String,
        val strFanart1: String,
        val strFanart2: String,
        val strFanart3: String,
        val strFanart4: String,
        val strGender: String,
        val strLeague: String,
        val strLeagueAlternate: String,
        val strLocked: String,
        val strLogo: String,
        val strNaming: String,
        val strPoster: String,
        val strRSS: String,
        val strSport: String,
        val strTrophy: String,
        val strTwitter: String,
        val strWebsite: String,
        val strYoutube: String
    )
}