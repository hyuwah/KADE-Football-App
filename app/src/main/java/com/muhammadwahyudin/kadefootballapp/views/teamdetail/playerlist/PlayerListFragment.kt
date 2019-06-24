package com.muhammadwahyudin.kadefootballapp.views.teamdetail.playerlist


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
import com.muhammadwahyudin.kadefootballapp.views.playerdetail.PlayerDetailActivity
import kotlinx.android.synthetic.main.fragment_player_list.*
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.intentFor
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 *
 */
class PlayerListFragment : Fragment() {

    companion object {
        const val TEAM_ID = "team_id_args"
        fun newInstance(teamId: String?) = PlayerListFragment().apply {
            arguments = bundleOf(TEAM_ID to teamId)
        }
    }

    private var teamId: String? = null
    private lateinit var adapter: PlayerListAdapter
    private val viewmodel: PlayerListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_player_list, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        teamId = arguments?.get(TEAM_ID) as String?

        adapter = PlayerListAdapter(listOf()) {}
        rv_player_list.layoutManager = LinearLayoutManager(act, RecyclerView.VERTICAL, false)
        rv_player_list.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        teamId?.let { id ->
            viewmodel.getPlayerList(id).observe(this,
                Observer { state ->
                    when (state) {
                        is LoadingState -> {
                            progressbar_player_list.visible()
                            tv_empty_view.invisible()
                        }
                        is PopulatedState -> {
                            progressbar_player_list.invisible()
                            adapter = PlayerListAdapter(state.data) { player ->
                                startActivity(
                                    intentFor<PlayerDetailActivity>(
                                        PlayerDetailActivity.PLAYER_PARCEL to player
                                    )
                                )
                            }
                            rv_player_list.adapter = adapter
                        }
                        is NoResultState -> {
                            progressbar_player_list.invisible()
                            tv_empty_view.visible()
                        }
                    }
                })

        }
    }


}
