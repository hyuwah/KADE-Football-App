package com.muhammadwahyudin.kadefootballapp.views.favoritematch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.app.invisible
import com.muhammadwahyudin.kadefootballapp.app.visible
import com.muhammadwahyudin.kadefootballapp.data.model.FavoriteEvent
import com.muhammadwahyudin.kadefootballapp.views.matchdetail.MatchDetailActivity
import kotlinx.android.synthetic.main.activity_favorite_match.*
import org.jetbrains.anko.intentFor
import org.koin.android.viewmodel.ext.android.viewModel

class FavoriteMatchActivity : AppCompatActivity() {

    private val favoriteMatchViewModel: FavoriteMatchViewModel by viewModel()
    lateinit var adapter: FavoriteMatchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_match)

        title = "Favorite Matches"

        tv_empty_view.invisible()
        progressbar_favorite_match.visible()
        rv_favorited_match.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        setupRecyclerViewAdapter(listOf())
    }

    override fun onResume() {
        super.onResume()
        favoriteMatchViewModel.loadFavoritedEvents(this).observe(
            this,
            Observer<List<FavoriteEvent>> { list ->
                progressbar_favorite_match.invisible()
                tv_empty_view.invisible()
                if (list.isNotEmpty()) {
                    setupRecyclerViewAdapter(list.reversed())
                } else {
                    setupRecyclerViewAdapter(listOf())
                    tv_empty_view.visible()
                }
            })
    }

    private fun setupRecyclerViewAdapter(list: List<FavoriteEvent>) {
        adapter = FavoriteMatchAdapter(list) { event ->
            startActivity(
                intentFor<MatchDetailActivity>(
                    MatchDetailActivity.MATCH_ID to event.eventId,
                    MatchDetailActivity.HOME_BADGE to event.teamHomeBadgeUrl,
                    MatchDetailActivity.AWAY_BADGE to event.teamAwayBadgeUrl
                )
            )
        }
        rv_favorited_match.adapter = adapter
    }
}
