package com.muhammadwahyudin.kadefootballapp.views.matchdetail

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.app.toReadableTimeWIB
import com.muhammadwahyudin.kadefootballapp.data.local.database
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage
import com.muhammadwahyudin.kadefootballapp.data.model.FavoriteEvent
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_match_detail.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.findOptional

class MatchDetailActivity : AppCompatActivity() {

    companion object {
        const val MATCH_ID = "match_id"
        const val HOME_BADGE = "home_badge"
        const val AWAY_BADGE = "away_badge"
    }

    private lateinit var matchId: String // TODO mesti support FavoriteEvent juga
    private var match: EventWithImage? = null
    private lateinit var homeBadge: String
    private lateinit var awayBadge: String

    private var isFavorited = false
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
            val viewModel = ViewModelProviders.of(this).get(MatchDetailViewModel::class.java)
            viewModel.loadMatchDetail(matchId).observe(this,
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
        menuInflater.inflate(R.menu.match_detail_menu, menu)
        favoriteBtn = menu?.findItem(R.id.favorite_action)
        favoriteBtn?.isVisible = false
        isFavorited()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.favorite_action -> {
                toggleFavoriteAction(item)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleFavoriteAction(item: MenuItem) {
        isFavorited = !isFavorited
        if (isFavorited) {
            item.setIcon(R.drawable.ic_favorite)
            addToFavorite()
        } else {
            item.setIcon(R.drawable.ic_favorite_border)
            removeFromFavorite()
            findOptional<View>(android.R.id.content)?.snackbar("Removed from favorite")
        }
    }

    private fun addToFavorite() {
        try {
            database.use {
                insert(
                    FavoriteEvent.TABLE_NAME,
                    FavoriteEvent.EVENT_ID to matchId,
                    FavoriteEvent.EVENT_DATE to match?.strTime?.toReadableTimeWIB(match?.dateEvent!!),
                    FavoriteEvent.EVENT_NAME to match?.strEvent,
                    FavoriteEvent.TEAM_HOME_NAME to match?.strHomeTeam,
                    FavoriteEvent.TEAM_HOME_SCORE to match?.intHomeScore,
                    FavoriteEvent.TEAM_HOME_BADGE_URL to homeBadge,
                    FavoriteEvent.TEAM_AWAY_NAME to match?.strAwayTeam,
                    FavoriteEvent.TEAM_AWAY_SCORE to match?.intAwayScore,
                    FavoriteEvent.TEAM_AWAY_BADGE_URL to awayBadge
                )
            }
            findOptional<View>(android.R.id.content)?.snackbar("Added to favorite")
        } catch (e: SQLiteConstraintException) {
            findOptional<View>(android.R.id.content)?.snackbar(e.localizedMessage)
        }
    }

    private fun removeFromFavorite() {
        try {
            database.use {
                delete(
                    FavoriteEvent.TABLE_NAME,
                    "(EVENT_ID = {id})",
                    "id" to matchId
                )
            }
            findOptional<View>(android.R.id.content)?.snackbar("Removed from favorite")
        } catch (e: SQLiteConstraintException) {
            findOptional<View>(android.R.id.content)?.snackbar(e.localizedMessage)
        }
    }

    private fun isFavorited() {
        database.use {
            val result = select(FavoriteEvent.TABLE_NAME)
                .whereArgs("(EVENT_ID = {id})", "id" to matchId)
            val favorite = result.parseList(classParser<FavoriteEvent>())
            if (favorite.isNotEmpty()) {
                isFavorited = true
                favoriteBtn?.setIcon(R.drawable.ic_favorite)
            } else {
                isFavorited = false
                favoriteBtn?.setIcon(R.drawable.ic_favorite_border)
            }
        }
    }
}
