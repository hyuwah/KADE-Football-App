package com.muhammadwahyudin.kadefootballapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.toast
import org.jetbrains.anko.verticalLayout

class LeaguesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val leagues = generateLeaguesData()

        verticalLayout {
            recyclerView {
                adapter = LeagueAdapter(leagues) { leagueModel ->
                    startActivity(intentFor<LeagueDetailActivity>(LeagueDetailActivity.LEAGUE_PARCEL to leagueModel))
                    toast("ID ${leagueModel.id}")
                }
                layoutManager = GridLayoutManager(context, 2)
            }
        }
    }

    private fun generateLeaguesData(): List<LeagueModel> {
        return ArrayList<LeagueModel>().apply {
            val names = resources.getStringArray(R.array.league_name)
            val ids = resources.getIntArray(R.array.league_id)
            val desc = resources.getStringArray(R.array.league_desc)
            val images = resources.obtainTypedArray(R.array.league_image)
            for (i in names.indices) {
                add(LeagueModel(names[i], ids[i], desc[i], images.getResourceId(i, 0)))
            }
            images.recycle()
        }
    }
}
