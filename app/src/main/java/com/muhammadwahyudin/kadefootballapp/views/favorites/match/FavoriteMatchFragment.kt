package com.muhammadwahyudin.kadefootballapp.views.favorites.match


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.app.invisible
import com.muhammadwahyudin.kadefootballapp.app.visible
import com.muhammadwahyudin.kadefootballapp.data.model.FavoriteEvent
import com.muhammadwahyudin.kadefootballapp.views.matchdetail.MatchDetailActivity
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.intentFor
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 *
 */
class FavoriteMatchFragment : Fragment() {

    private val favoriteMatchViewModel: FavoriteMatchViewModel by viewModel()
    lateinit var adapter: FavoriteMatchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_empty_view.invisible()
        progressbar_favorite_match.visible()
        rv_favorited_match.layoutManager = LinearLayoutManager(act, RecyclerView.VERTICAL, false)
        setupRecyclerViewAdapter(listOf())

    }

    override fun onResume() {
        super.onResume()
        favoriteMatchViewModel.loadFavoritedEvents().observe(this,
            Observer { list ->
                progressbar_favorite_match.invisible()
                tv_empty_view.invisible()
                if (list.isNotEmpty())
                    setupRecyclerViewAdapter(list.reversed())
                else {
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
