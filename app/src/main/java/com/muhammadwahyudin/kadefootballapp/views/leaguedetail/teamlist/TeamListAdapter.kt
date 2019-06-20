package com.muhammadwahyudin.kadefootballapp.views.leaguedetail.teamlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.data.model.Team
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.team_item.view.*

class TeamListAdapter(
    private val teams: List<Team>,
    private val clickListener: (Team) -> Unit
) :
    RecyclerView.Adapter<TeamListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.team_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(teams[position], clickListener)
    }

    override fun getItemCount(): Int = teams.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(team: Team, clickListener: (Team) -> Unit) = with(itemView) {
            tv_name.text = team.strTeam
            tv_country.text = team.strCountry
            tv_stadium.text = team.strStadium
            tv_stadium_location.text = team.strStadiumLocation
            Picasso.get().load(team.strTeamBadge).into(iv_badge)
            setOnClickListener { clickListener(team) }
        }
    }
}