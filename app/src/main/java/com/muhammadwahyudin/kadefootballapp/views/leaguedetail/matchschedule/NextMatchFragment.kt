package com.muhammadwahyudin.kadefootballapp.views.leaguedetail.matchschedule

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
import com.muhammadwahyudin.kadefootballapp.data.model.EmptyState
import com.muhammadwahyudin.kadefootballapp.data.model.LoadingState
import com.muhammadwahyudin.kadefootballapp.data.model.NoResultState
import com.muhammadwahyudin.kadefootballapp.data.model.PopulatedState
import com.muhammadwahyudin.kadefootballapp.views.matchdetail.MatchDetailActivity
import kotlinx.android.synthetic.main.next_match_fragment.*
import org.jetbrains.anko.support.v4.intentFor
import org.koin.android.viewmodel.ext.android.viewModel

class NextMatchFragment : Fragment() {

    private val scheduleViewModel: MatchScheduleViewModel by viewModel()
    lateinit var adapter: MatchesScheduleAdapter
    private var mLeagueId: String? = null

    companion object {
        private const val LEAGUE_ID = "league_id"
        fun newInstance(leagueId: String? = null) = NextMatchFragment().apply {
            arguments = bundleOf(LEAGUE_ID to leagueId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.next_match_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLeagueId = arguments?.getString(LEAGUE_ID)
        // Prepare recyclerview & adapter
        adapter = MatchesScheduleAdapter(listOf()) { event ->
            startActivity(
                intentFor<MatchDetailActivity>(
                    MatchDetailActivity.MATCH_ID to event.idEvent,
                    MatchDetailActivity.HOME_BADGE to event.strHomeTeamBadge,
                    MatchDetailActivity.AWAY_BADGE to event.strAwayTeamBadge
                )
            )
        }
        rv_next_match.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv_next_match.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mLeagueId?.let {
            scheduleViewModel.getNextEventsState(it).observe(this, Observer { state ->
                when (state) {
                    is EmptyState -> {
                    }
                    is LoadingState -> {
                        progressbar_next_match.visible()
                        tv_empty_view.invisible()
                    }
                    is PopulatedState -> {
                        progressbar_next_match.invisible()
                        adapter = MatchesScheduleAdapter(state.data) { event ->
                            startActivity(
                                intentFor<MatchDetailActivity>(
                                    MatchDetailActivity.MATCH_ID to event.idEvent,
                                    MatchDetailActivity.HOME_BADGE to event.strHomeTeamBadge,
                                    MatchDetailActivity.AWAY_BADGE to event.strAwayTeamBadge
                                )
                            )
                        }
                        rv_next_match.adapter = adapter
                    }
                    is NoResultState -> {
                        progressbar_next_match.invisible()
                        tv_empty_view.visible()
                    }
                }
            })
        }
    }

}
