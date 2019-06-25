package com.muhammadwahyudin.kadefootballapp.views.leaguedetail.standings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.data.model.Standing
import kotlinx.android.synthetic.main.standings_item.view.*

class StandingsAdapter(private var standings: List<Standing>) :
    RecyclerView.Adapter<StandingsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.standings_item, parent, false))


    override fun getItemCount(): Int = standings.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(standings[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(standing: Standing) = with(itemView) {
            tv_standing_team.text = standing.name
            tv_standing_play.text = standing.played.toString()
            tv_standing_win.text = standing.win.toString()
            tv_standing_draw.text = standing.draw.toString()
            tv_standing_lose.text = standing.loss.toString()
            tv_standing_gf.text = standing.goalsfor.toString()
            tv_standing_ga.text = standing.goalsagainst.toString()
            tv_standing_gd.text = standing.goalsdifference.toString()
            tv_standing_points.text = standing.total.toString()
        }
    }
}