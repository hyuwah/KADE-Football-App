package com.muhammadwahyudin.kadefootballapp.views.matchdetail

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_match_detail.*

class MatchDetailActivity : AppCompatActivity() {

    companion object {
        const val MATCH_PARCEL = "match_parcel"
        const val HOME_BADGE = "home_badge"
        const val AWAY_BADGE = "away_badge"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_detail)

        val match = intent.getParcelableExtra<EventWithImage>(MATCH_PARCEL)
        val homeBadge = intent.getStringExtra(HOME_BADGE)
        val awayBadge = intent.getStringExtra(AWAY_BADGE)
        Log.d("test", awayBadge ?: "null")

        title = match.strEvent
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tv_test.text = match.strEvent

        tv_home_team_name.text = match.strHomeTeam
        tv_home_score.text = match.intHomeScore
        homeBadge?.let { Picasso.get().load(homeBadge).into(iv_home_badge) }
        tv_away_team_name.text = match.strAwayTeam
        tv_away_score.text = match.intAwayScore
        awayBadge?.let { Picasso.get().load(awayBadge).into(iv_away_badge) }


    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
