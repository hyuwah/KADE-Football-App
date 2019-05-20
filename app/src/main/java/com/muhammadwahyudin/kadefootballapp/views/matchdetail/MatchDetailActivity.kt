package com.muhammadwahyudin.kadefootballapp.views.matchdetail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.app.toReadableTimeWIB
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_match_detail.*

class MatchDetailActivity : AppCompatActivity() {

    companion object {
        const val MATCH_PARCEL = "match_parcel"
        const val HOME_BADGE = "home_badge"
        const val AWAY_BADGE = "away_badge"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_detail)

        val match = intent.getParcelableExtra<EventWithImage>(MATCH_PARCEL)
        val homeBadge = intent.getStringExtra(HOME_BADGE)
        val awayBadge = intent.getStringExtra(AWAY_BADGE)

        title = match.strEvent
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tv_datetime.text = match.strTime?.toReadableTimeWIB(match.dateEvent!!)

        tv_home_team_name.text = match.strHomeTeam
        tv_home_score.text = match.intHomeScore ?: "-"
        homeBadge?.let { Picasso.get().load(homeBadge).into(iv_home_badge) }
        tv_away_team_name.text = match.strAwayTeam
        tv_away_score.text = match.intAwayScore ?: "-"
        awayBadge?.let { Picasso.get().load(awayBadge).into(iv_away_badge) }

        tv_home_goals.text = formatContent(match.strHomeGoalDetails) + "\n"
        tv_away_goals.text = formatContent(match.strAwayGoalDetails) + "\n"

        tv_home_shots.text = match.intHomeShots ?: "-\n"
        tv_away_shots.text = match.intAwayShots ?: "-\n"

        tv_home_cards.text = "[Yellow]\n" + formatContent(match.strHomeYellowCards) + "\n" +
                "[Red]\n" + formatContent(match.strHomeRedCards)
        tv_away_cards.text = "[Yellow]\n" + formatContent(match.strAwayYellowCards) + "\n" +
                "[Red]\n" + formatContent(match.strAwayRedCards)

        tv_home_lineup.text = "[Forward]\n" + formatContent(match.strHomeLineupForward) +
                "\n[Midfield]\n" + formatContent(match.strHomeLineupMidfield) +
                "\n[Defense]\n" + formatContent(match.strHomeLineupDefense) +
                "\n[Goalkeeper]\n" + formatContent(match.strHomeLineupGoalkeeper) +
                "\n[Substitutes]\n" + formatContent(match.strHomeLineupSubstitutes)
        tv_away_lineup.text = "[Forward]\n" + formatContent(match.strAwayLineupForward) +
                "\n[Midfield]\n" + formatContent(match.strAwayLineupMidfield) +
                "\n[Defense]\n" + formatContent(match.strAwayLineupDefense) +
                "\n[Goalkeeper]\n" + formatContent(match.strAwayLineupGoalkeeper) +
                "\n[Substitutes]\n" + formatContent(match.strAwayLineupSubstitutes)

        tv_home_formation.text = match.strHomeFormation ?: "-"
        tv_away_formation.text = match.strAwayFormation ?: "-"

    }

    private fun formatContent(string: String?): String {
        return if (string.isNullOrEmpty()) {
            "-"
        } else {
            string.replace(";", "\n").trim()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
