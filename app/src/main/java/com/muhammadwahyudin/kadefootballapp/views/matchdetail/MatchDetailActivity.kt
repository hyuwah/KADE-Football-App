package com.muhammadwahyudin.kadefootballapp.views.matchdetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.app.DB_OPS_STATE.*
import com.muhammadwahyudin.kadefootballapp.app.toReadableTimeWIB
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_match_detail.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.findOptional
import org.koin.android.viewmodel.ext.android.viewModel

class MatchDetailActivity : AppCompatActivity() {

    companion object {
        const val MATCH_ID = "match_id"
        const val HOME_BADGE = "home_badge"
        const val AWAY_BADGE = "away_badge"
    }

    private val mViewModel: MatchDetailViewModel by viewModel()

    private lateinit var matchId: String
    private var match: EventWithImage? = null
    private lateinit var homeBadge: String
    private lateinit var awayBadge: String

    private var favoriteBtn: MenuItem? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_detail)

        matchId = intent.getStringExtra(MATCH_ID)
        homeBadge = intent.getStringExtra(HOME_BADGE)
        awayBadge = intent.getStringExtra(AWAY_BADGE)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Default Init View
        title = "Match Detail"
        Picasso.get().load(homeBadge).into(iv_home_badge)
        Picasso.get().load(awayBadge).into(iv_away_badge)

        if (matchId.isNotEmpty()) {
            //fetch match detail from viewmodel
            mViewModel.loadMatchDetail(matchId).observe(
                this,
                Observer<EventWithImage> { event ->
                    event.strAwayTeamBadge = awayBadge
                    event.strHomeTeamBadge = homeBadge
                    match = event
                    setUiContent(match!!)
                    favoriteBtn?.isVisible = true
                })
        }

    }

    private fun setUiContent(event: EventWithImage) {
        title = event.strEvent
        tv_datetime.text = event.strTime?.toReadableTimeWIB(event.dateEvent!!)
        tv_league.text = event.strLeague

        tv_home_team_name.text = event.strHomeTeam
        tv_home_score.text = event.intHomeScore ?: "-"

        tv_away_team_name.text = event.strAwayTeam
        tv_away_score.text = event.intAwayScore ?: "-"

        tv_home_goals.text = String.format("%s\n", formatContent(event.strHomeGoalDetails))
        tv_away_goals.text = String.format("%s\n", formatContent(event.strAwayGoalDetails))

        tv_home_shots.text = event.intHomeShots ?: "-\n"
        tv_away_shots.text = event.intAwayShots ?: "-\n"

        tv_home_cards.text = String.format(
            "[Yellow]\n%s\n\n[Red]\n%s",
            formatContent(event.strHomeYellowCards),
            formatContent(event.strHomeRedCards)
        )
        tv_away_cards.text = String.format(
            "[Yellow]\n%s\n\n[Red]\n%s",
            formatContent(event.strAwayYellowCards),
            formatContent(event.strAwayRedCards)
        )

        tv_home_lineup.text = String.format(
            "[Forward]\n%s\n\n[Midfield]\n%s\n\n[Defense]\n%s\n\n[Goalkeeper]\n%s\n\n[Substitutes]\n",
            formatContent(event.strHomeLineupForward),
            formatContent(event.strHomeLineupMidfield),
            formatContent(event.strHomeLineupDefense),
            formatContent(event.strHomeLineupGoalkeeper),
            formatContent(event.strHomeLineupSubstitutes)
        )
        tv_away_lineup.text = String.format(
            "[Forward]\n%s\n\n[Midfield]\n%s\n\n[Defense]\n%s\n\n[Goalkeeper]\n%s\n\n[Substitutes]\n",
            formatContent(event.strAwayLineupForward),
            formatContent(event.strAwayLineupMidfield),
            formatContent(event.strAwayLineupDefense),
            formatContent(event.strAwayLineupGoalkeeper),
            formatContent(event.strAwayLineupSubstitutes)
        )

        tv_home_formation.text = event.strHomeFormation ?: "-"
        tv_away_formation.text = event.strAwayFormation ?: "-"
    }

    private fun formatContent(string: String?): String {
        return if (string.isNullOrEmpty()) {
            "-"
        } else {
            string.replace(Regex("; ?"), "\n").trim()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_favorite_menu, menu)
        favoriteBtn = menu?.findItem(R.id.favorite_action)
        favoriteBtn?.isVisible = false

        mViewModel.isFavoritedMatch(matchId).observe(this,
            Observer {
                if (it) favoriteBtn?.setIcon(R.drawable.ic_favorite)
                else favoriteBtn?.setIcon(R.drawable.ic_favorite_border)
            })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.favorite_action -> {
                mViewModel.updateFavoriteState().observe(this,
                    Observer {
                        when (it) {
                            INSERT_SUCCESS -> findOptional<View>(android.R.id.content)?.snackbar("Added to favorite")
                            REMOVE_SUCCESS -> findOptional<View>(android.R.id.content)?.snackbar("Removed from favorite")
                            ERROR -> findOptional<View>(android.R.id.content)?.snackbar("Database Operation Failed")
                            else -> {
                            }
                        }
                    })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
