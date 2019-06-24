package com.muhammadwahyudin.kadefootballapp.views.favorites.match

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.data.model.FavoriteEvent
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.event_match_item.view.*

class FavoriteMatchAdapter(
    private val events: List<FavoriteEvent>,
    private val clickListener: (FavoriteEvent) -> Unit
) : RecyclerView.Adapter<FavoriteMatchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.event_match_item, parent, false))
    }

    override fun getItemCount(): Int = events.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(events[position], clickListener)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(event: FavoriteEvent, clickListener: (FavoriteEvent) -> Unit) = with(itemView) {
            tv_match_title.text = event.eventName
            tv_match_date.text = event.eventDate
            tv_home_score.text = if (event.teamHomeScore.isNullOrEmpty()) "-" else event.teamHomeScore
            tv_home_team_name.text = event.teamHomeName
            tv_away_score.text = if (event.teamAwayScore.isNullOrEmpty()) "-" else event.teamAwayScore
            tv_away_team_name.text = event.teamAwayName
            Picasso.get().load(event.teamHomeBadgeUrl).into(iv_badge_home)
            Picasso.get().load(event.teamAwayBadgeUrl).into(iv_badge_away)
            setOnClickListener { clickListener(event) }
        }
    }
}