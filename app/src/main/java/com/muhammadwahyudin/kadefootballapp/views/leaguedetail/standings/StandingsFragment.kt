package com.muhammadwahyudin.kadefootballapp.views.leaguedetail.standings


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.app.invisible
import com.muhammadwahyudin.kadefootballapp.app.visible
import com.muhammadwahyudin.kadefootballapp.data.model.LoadingState
import com.muhammadwahyudin.kadefootballapp.data.model.NoResultState
import com.muhammadwahyudin.kadefootballapp.data.model.PopulatedState
import kotlinx.android.synthetic.main.fragment_standings.*
import org.jetbrains.anko.support.v4.act
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 *
 */
class StandingsFragment : Fragment() {

    companion object {
        const val LEAGUE_ID = "league_id"
        fun newInstance(leagueId: String?) = StandingsFragment().apply {
            arguments = bundleOf(LEAGUE_ID to leagueId)
        }
    }

    private var leagueId: String? = null
    private val viewModel: StandingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_standings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        leagueId = arguments?.get(LEAGUE_ID) as String?

        var adapter = StandingsAdapter(listOf())
        rv_standings.layoutManager = LinearLayoutManager(act, RecyclerView.VERTICAL, false)
        rv_standings.addItemDecoration(DividerItemDecoration(act, VERTICAL))
        rv_standings.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        leagueId?.let { id ->
            // Fetch standings from viewmodel
            viewModel.getLeagueStandings(id).observe(this, Observer { state ->
                var adapter = StandingsAdapter(listOf())
                when (state) {
                    is LoadingState -> {
                        progressbar_standings.visible()
                        tv_empty_view.invisible()
                        ll_standings.invisible()
                    }
                    is PopulatedState -> {
                        progressbar_standings.invisible()
                        tv_empty_view.invisible()
                        ll_standings.visible()
                        adapter = StandingsAdapter(state.data)
                    }
                    is NoResultState -> {
                        progressbar_standings.invisible()
                        tv_empty_view.visible()
                        ll_standings.invisible()
                    }
                }
                rv_standings.adapter = adapter
            })
        }
    }


}
