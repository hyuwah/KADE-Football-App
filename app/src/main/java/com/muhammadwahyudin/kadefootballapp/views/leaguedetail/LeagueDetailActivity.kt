package com.muhammadwahyudin.kadefootballapp.views.leaguedetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.data.model.League
import com.muhammadwahyudin.kadefootballapp.data.remote.response.LeagueDetailRes
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_league_detail.*

class LeagueDetailActivity : AppCompatActivity() {

    companion object {
        const val LEAGUE_PARCEL = "league_parcel"
    }

    private lateinit var mViewModel: LeagueDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_league_detail)

        setSupportActionBar(toolbar)

        val leagueModel = intent.getParcelableExtra<League>(LEAGUE_PARCEL)
        ct_layout.title = leagueModel.name
        ct_layout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.white))
        ct_layout.setCollapsedTitleTextColor(ContextCompat.getColor(this, android.R.color.white))


        mViewModel = ViewModelProviders.of(this).get(LeagueDetailViewModel::class.java)
        mViewModel.getLeagueDetail(leagueModel.id).observe(this,
            Observer<LeagueDetailRes.League> { league ->
                Picasso.get().load(league?.strFanart1).into(iv_league_detail_banner)
                Picasso.get().load(league?.strBadge).into(civ_league_detail)
                tv_title_league.text = league?.strLeagueAlternate
                // reinit viewpager + fragment
            })


        val vpAdapter = ViewPagerAdapter(supportFragmentManager)
        // Add fragment kosong
        vpAdapter.addFragment(PreviousMatchFragment(), "Previous Match")
        vpAdapter.addFragment(NextMatchFragment("Custom Text"), "Next Match")
        viewpager.adapter = vpAdapter
        tablayout.setupWithViewPager(viewpager)
    }
}
