package com.muhammadwahyudin.kadefootballapp.data.model

data class FavoriteEvent(
    val id: Long?,
    val eventId: String?,
    val eventDate: String?,
    val eventName: String?,
    val teamHomeName: String?,
    val teamHomeScore: String?,
    val teamHomeBadgeUrl: String?,
    val teamAwayName: String?,
    val teamAwayScore: String?,
    val teamAwayBadgeUrl: String?
) {
    companion object {
        const val TABLE_NAME = "TABLE_FAVORITE_EVENT"
        const val ID = "ID"
        const val EVENT_ID = "EVENT_ID"
        const val EVENT_DATE = "EVENT_DATE"
        const val EVENT_NAME = "EVENT_NAME"
        const val TEAM_HOME_NAME = "TEAM_HOME_NAME"
        const val TEAM_HOME_SCORE = "TEAM_HOME_SCORE"
        const val TEAM_HOME_BADGE_URL = "TEAM_HOME_BADGE_URL"
        const val TEAM_AWAY_NAME = "TEAM_AWAY_NAME"
        const val TEAM_AWAY_SCORE = "TEAM_AWAY_SCORE"
        const val TEAM_AWAY_BADGE_URL = "TEAM_AWAY_BADGE_URL"
    }
}