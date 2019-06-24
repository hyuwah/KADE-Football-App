package com.muhammadwahyudin.kadefootballapp.views.favorites.teams


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
import com.muhammadwahyudin.kadefootballapp.data.model.Team
import com.muhammadwahyudin.kadefootballapp.views.teamdetail.TeamDetailActivity
import kotlinx.android.synthetic.main.fragment_favorites.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.intentFor
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 *
 */
class FavoriteTeamFragment : Fragment() {

    private val favoriteTeamViewModel: FavoriteTeamViewModel by viewModel()
    lateinit var adapter: FavoriteTeamAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_favorites, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_empty_view.invisible()
        tv_empty_view.setText(R.string.no_team_found)
        progressbar_favorite_match.visible()
        rv_favorited_match.layoutManager = LinearLayoutManager(act, RecyclerView.VERTICAL, false)
        setupRecyclerViewAdapter(listOf())

    }

    override fun onResume() {
        super.onResume()
        favoriteTeamViewModel.loadFavoritedTeam().observe(this,
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

    private fun setupRecyclerViewAdapter(list: List<Team>) {
        adapter = FavoriteTeamAdapter(list) { team ->
            startActivity(
                intentFor<TeamDetailActivity>(
                    TeamDetailActivity.TEAM_PARCEL to team
                )
            )
        }
        rv_favorited_match.adapter = adapter
    }

}
