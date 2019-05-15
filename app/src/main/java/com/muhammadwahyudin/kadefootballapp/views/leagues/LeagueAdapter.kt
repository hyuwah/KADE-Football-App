package com.muhammadwahyudin.kadefootballapp.views.leagues

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.data.model.League
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.find

class LeagueAdapter(private val leagues: List<League>, val clickListener: (League) -> Unit) :
    RecyclerView.Adapter<LeagueAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindLeague(leagues[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LeagueItemUI().createView(
            AnkoContext.Companion.create(parent.context, parent)
        )
    ).apply {
        itemView.setOnClickListener {
            val pos = this.adapterPosition
            if (pos != NO_POSITION) clickListener(leagues[pos])
        }
    }

    override fun getItemCount(): Int = leagues.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.find(R.id.league_item_image)
        val name: TextView = view.find(R.id.league_item_name)
        fun bindLeague(league: League) {
            name.text = league.name
            image.setImageResource(league.logoRes)
        }
    }
}