package com.muhammadwahyudin.kadefootballapp.views.teamdetail.playerlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.data.model.Player
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.player_item.view.*

class PlayerListAdapter(
    private val players: List<Player>,
    private val clickListener: (Player) -> Unit
) : RecyclerView.Adapter<PlayerListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.player_item, parent, false))

    override fun getItemCount(): Int = players.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(players[position], clickListener)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(player: Player, clickListener: (Player) -> Unit) = with(itemView) {
            Picasso.get().load(player.strCutout).placeholder(R.drawable.placeholder_avatar_male).into(civ_player)
            tv_name.text = player.strPlayer
            tv_position.text = "${player.strNumber ?: "-"} | " + player.strPosition
            tv_nationality.text = player.strNationality
            // TODO More card data
            setOnClickListener { clickListener(player) }
        }
    }
}