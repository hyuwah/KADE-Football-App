package com.muhammadwahyudin.kadefootballapp.views.teamdetail


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.app.gone
import com.muhammadwahyudin.kadefootballapp.app.visible
import com.muhammadwahyudin.kadefootballapp.data.model.Team
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_team_detail.*
import org.jetbrains.anko.childrenRecursiveSequence

/**
 * A simple [Fragment] subclass.
 *
 */
class TeamDetailFragment : Fragment() {

    private var team: Team? = null

    companion object {
        const val TEAM = "team_arg"
        fun newInstance(team: Team) = TeamDetailFragment().apply {
            arguments = bundleOf(TEAM to team)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_team_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        team = arguments?.get(TEAM) as Team?
        team?.let {
            view.childrenRecursiveSequence().forEach { view ->
                view.visible()
            }
            // TODO More Info & design
            progressbar_team_info.gone()
            Picasso.get().load(it.strTeamJersey).into(iv_jersey)
            tv_formed_year.text = getString(R.string.year_founded_title) + it.intFormedYear
            tv_team_desc.text = it.strDescriptionEN
            Picasso.get().load(it.strTeamBanner).into(iv_banner)

            Picasso.get().load(it.strStadiumThumb).into(iv_stadium)
            tv_stadium.text = it.strStadium
            tv_stadium_loc.text = it.strStadiumLocation
        }
    }


}
