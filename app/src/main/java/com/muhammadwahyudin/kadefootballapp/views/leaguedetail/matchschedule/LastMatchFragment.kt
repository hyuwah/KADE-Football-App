package com.muhammadwahyudin.kadefootballapp.views.leaguedetail.matchschedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.app.invisible
import com.muhammadwahyudin.kadefootballapp.app.visible
import com.muhammadwahyudin.kadefootballapp.views.matchdetail.MatchDetailActivity
import kotlinx.android.synthetic.main.last_match_fragment.*
import org.jetbrains.anko.support.v4.intentFor
import org.jetbrains.anko.support.v4.toast

class LastMatchFragment : Fragment() {

    private lateinit var scheduleViewModel: MatchScheduleViewModel
    lateinit var adapter: MatchesScheduleAdapter
    private var mLeagueId: String? = null

    companion object {
        private const val LEAGUE_ID = "league_id"
        fun newInstance(leagueId: String?) = LastMatchFragment().apply {
            arguments = bundleOf(LEAGUE_ID to leagueId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.last_match_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mLeagueId = arguments?.getString(LEAGUE_ID)
        progressbar_last_match.visible()
        tv_empty_view.invisible()
        // Prepare recyclerview & adapter
        adapter = MatchesScheduleAdapter(listOf()) { event ->
            startActivity(intentFor<MatchDetailActivity>(MatchDetailActivity.MATCH_PARCEL to event))
            toast(event.idEvent)
        }
        rv_last_match.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv_last_match.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        scheduleViewModel = ViewModelProviders.of(this).get(MatchScheduleViewModel::class.java)
        mLeagueId?.let {
            scheduleViewModel.getLastEvents(it).observe(this, Observer { events ->
                progressbar_last_match.invisible()
                if (events.isNotEmpty()) {
                    // update adapter
                    adapter = MatchesScheduleAdapter(events) { event ->
                        startActivity(intentFor<MatchDetailActivity>(MatchDetailActivity.MATCH_PARCEL to event))
                        toast(event.idEvent)
                    }
                    rv_last_match.adapter = adapter
                } else {
                    // show empty view
                    tv_empty_view.invisible()
                }
            })
        }

    }

}
