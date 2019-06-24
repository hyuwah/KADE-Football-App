package com.muhammadwahyudin.kadefootballapp.views.teamdetail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.app.DB_OPS_STATE.*
import com.muhammadwahyudin.kadefootballapp.data.model.Team
import com.muhammadwahyudin.kadefootballapp.views._utils.ViewPagerAdapter
import com.muhammadwahyudin.kadefootballapp.views.teamdetail.playerlist.PlayerListFragment
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_team_detail.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.findOptional
import org.koin.android.viewmodel.ext.android.viewModel

class TeamDetailActivity : AppCompatActivity() {

    companion object {
        const val TEAM_PARCEL = "team_parcel"
    }

    private val mViewModel: TeamDetailViewModel by viewModel()
    private lateinit var team: Team

    private var favoriteBtn: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)
        setSupportActionBar(toolbar)

        team = intent.getParcelableExtra(TEAM_PARCEL)
        ct_layout.title = team.strTeam
        ct_layout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.white))
        ct_layout.setCollapsedTitleTextColor(ContextCompat.getColor(this, android.R.color.white))

        Picasso.get().load(team.strTeamBadge).into(civ_team_badge)
        Picasso.get().load(team.strTeamFanart4).into(iv_team_detail_banner)

        tv_team_name.text = team.strAlternate
        tv_country.text = team.strLeague

        tablayout.setupWithViewPager(viewpager)
        initViewPagerFragment()
    }

    private fun initViewPagerFragment() {
        val vpAdapter = ViewPagerAdapter(supportFragmentManager).apply {
            addFragment(TeamDetailFragment.newInstance(team), "Info")
            addFragment(PlayerListFragment.newInstance(team.idTeam), "Player")
        }
        viewpager.adapter = vpAdapter
    }

    /// FAVORITE MENU

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_favorite_menu, menu)
        favoriteBtn = menu?.findItem(R.id.favorite_action)
        favoriteBtn?.isVisible = true

        mViewModel.isFavoritedTeam(team.idTeam).observe(this,
            Observer {
                if (it) favoriteBtn?.setIcon(R.drawable.ic_favorite)
                else favoriteBtn?.setIcon(R.drawable.ic_favorite_border)
            })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.favorite_action -> {
                mViewModel.updateFavoriteState(team).observe(this, Observer {
                    when (it) {
                        INSERT_SUCCESS -> findOptional<View>(android.R.id.content)?.snackbar("Added to favorite")
                        REMOVE_SUCCESS -> findOptional<View>(android.R.id.content)?.snackbar("Removed from favorite")
                        ERROR -> findOptional<View>(android.R.id.content)?.snackbar("Database operation failed")
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
