package com.muhammadwahyudin.kadefootballapp.views.leaguedetail.nextmatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.app.invisible
import com.muhammadwahyudin.kadefootballapp.views.leaguedetail.lastmatch.LastMatchAdapter
import kotlinx.android.synthetic.main.next_match_fragment.*
import org.jetbrains.anko.support.v4.toast

class NextMatchFragment(var leagueId: String? = null) : Fragment() {

    private lateinit var viewModel: NextMatchViewModel
    lateinit var adapter: LastMatchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.next_match_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressbar_next_match.visibility
        // Prepare recyclerview & adapter
        adapter = LastMatchAdapter(listOf()) { event ->
            toast(event.idEvent)
        }
        rv_next_match.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv_next_match.adapter = adapter

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NextMatchViewModel::class.java)
        leagueId?.let {
            viewModel.getEvents(it).observe(this, Observer { events ->
                progressbar_next_match.invisible()
                // update adapter
                if (events.isNotEmpty()) {
                    adapter = LastMatchAdapter(events) { event ->
                        toast(event.idEvent)
                    }
                    rv_next_match.adapter = adapter
                } else {
                    // show empty view
                }
            })
        }
    }

}
