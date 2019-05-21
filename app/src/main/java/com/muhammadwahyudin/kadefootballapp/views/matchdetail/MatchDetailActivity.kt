package com.muhammadwahyudin.kadefootballapp.views.matchdetail

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
        const val MATCH_PARCEL = "match_parcel"
        const val HOME_BADGE = "home_badge"
        const val AWAY_BADGE = "away_badge"
    }

    var isFavorited = false
    lateinit var match: EventWithImage
    lateinit var homeBadge: String
    lateinit var awayBadge: String
    var favoriteBtn: MenuItem? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_detail)

        match = intent.getParcelableExtra(MATCH_PARCEL)
        homeBadge = intent.getStringExtra(HOME_BADGE)
        awayBadge = intent.getStringExtra(AWAY_BADGE)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.match_detail_menu, menu)
        favoriteBtn = menu?.findItem(R.id.favorite_action)
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
                    FavoriteEvent.EVENT_ID to match.idEvent,
                    FavoriteEvent.EVENT_DATE to match.strTime?.toReadableTimeWIB(match.dateEvent!!),
                    FavoriteEvent.EVENT_NAME to match.strEvent,
                    FavoriteEvent.TEAM_HOME_NAME to match.strHomeTeam,
                    FavoriteEvent.TEAM_HOME_SCORE to match.intHomeScore,
                    FavoriteEvent.TEAM_HOME_BADGE_URL to homeBadge,
                    FavoriteEvent.TEAM_AWAY_NAME to match.strAwayTeam,
                    FavoriteEvent.TEAM_AWAY_SCORE to match.intAwayScore,
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
                    "id" to match.idEvent
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
                .whereArgs("(EVENT_ID = {id})", "id" to match.idEvent)
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
