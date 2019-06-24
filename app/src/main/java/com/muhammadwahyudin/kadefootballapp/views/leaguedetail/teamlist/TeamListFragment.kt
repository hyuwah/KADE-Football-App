package com.muhammadwahyudin.kadefootballapp.views.leaguedetail.teamlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.app.invisible
import com.muhammadwahyudin.kadefootballapp.app.visible
import com.muhammadwahyudin.kadefootballapp.data.model.LoadingState
import com.muhammadwahyudin.kadefootballapp.data.model.NoResultState
import com.muhammadwahyudin.kadefootballapp.data.model.PopulatedState
import com.muhammadwahyudin.kadefootballapp.views.teamdetail.TeamDetailActivity
import kotlinx.android.synthetic.main.team_list_fragment.*
import org.jetbrains.anko.support.v4.intentFor
import org.koin.android.viewmodel.ext.android.viewModel

class TeamListFragment : Fragment() {

    private val teamListViewModel: TeamListViewModel by viewModel()
    lateinit var adapter: TeamListAdapter
    private var mLeagueId: String? = null

    companion object {
        private const val LEAGUE_ID = "league_id"
        fun newInstance(leagueId: String?) = TeamListFragment().apply {
            arguments = bundleOf(LEAGUE_ID to leagueId)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.team_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLeagueId = arguments?.getString(LEAGUE_ID)
        adapter = TeamListAdapter(listOf()) { team ->
            startActivity(
                intentFor<TeamDetailActivity>()
            )
        }
        rv_team_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv_team_list.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mLeagueId?.let { leagueId ->
            teamListViewModel.getTeamList(leagueId).observe(this, Observer { state ->
                when (state) {
                    is LoadingState -> {
                        progressbar_team_list.visible()
                        tv_empty_view.invisible()
                    }
                    is PopulatedState -> {
                        progressbar_team_list.invisible()
                        adapter = TeamListAdapter(state.data) { team ->
                            startActivity(
                                intentFor<TeamDetailActivity>(
                                    TeamDetailActivity.TEAM_PARCEL to team
                                )
                            )
                        }
                        rv_team_list.adapter = adapter
                    }
                    is NoResultState -> {
                        progressbar_team_list.invisible()
                        tv_empty_view.visible()
                    }
                }
            })
        }
    }
}