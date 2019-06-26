package com.muhammadwahyudin.kadefootballapp.views.playerdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.data.model.Player
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_player_detail.*

class PlayerDetailActivity : AppCompatActivity() {

    companion object {
        const val PLAYER_PARCEL = "player_parcel"
    }

    private var player: Player? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player_detail)

        setSupportActionBar(toolbar)

        player = intent.getParcelableExtra(PLAYER_PARCEL)
        ct_layout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.white))
        ct_layout.setCollapsedTitleTextColor(ContextCompat.getColor(this, android.R.color.white))

        player?.let {
            ct_layout.title = it.strPlayer
            Picasso.get().load(it.strCutout).placeholder(R.drawable.placeholder_avatar_male).into(civ_player_avatar)
            Picasso.get().load(it.strFanart1).placeholder(R.drawable.favorite_match_bg).into(iv_player_detail_banner)
            tv_player_name.text = "${it.strNumber ?: "-"} | " + it.strPosition
            tv_country.text = it.strNationality
            tv_player_desc.text = it.strDescriptionEN
            tv_dob.text = "D.O.B: ${it.strBirthLocation}, ${it.dateBorn}"
            tv_wage.text = "Wage : ${if (it.strWage.isNullOrEmpty()) "-" else it.strWage}"
            tv_height_weight.text =
                "Height : ${it.strHeight}\tWeight: ${if (it.strWeight.isNullOrEmpty()) "-" else it.strWeight}"
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
