package com.muhammadwahyudin.kadefootballapp.views.leaguedetail.matchschedule

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.app.toReadableTimeWIB
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.event_match_item.view.*

class MatchesScheduleAdapter(
    private val events: List<EventWithImage>,
    private val clickListener: (EventWithImage) -> Unit
) :
    RecyclerView.Adapter<MatchesScheduleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.event_match_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(events[position], clickListener)
    }

    override fun getItemCount(): Int = events.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(event: EventWithImage, clickListener: (EventWithImage) -> Unit) = with(itemView) {
            tv_match_title.text = event.strEvent
            tv_match_date.text = event.strTime?.toReadableTimeWIB(event.dateEvent!!)
            tv_home_score.text = if (event.intHomeScore.isNullOrEmpty()) "-" else event.intHomeScore
            tv_home_team_name.text = event.strHomeTeam
            tv_away_score.text = if (event.intAwayScore.isNullOrEmpty()) "-" else event.intAwayScore
            tv_away_team_name.text = event.strAwayTeam
            Picasso.get().load(event.strHomeTeamBadge).into(iv_badge_home)
            Picasso.get().load(event.strAwayTeamBadge).into(iv_badge_away)
            setOnClickListener { clickListener(event) }
        }
    }
}