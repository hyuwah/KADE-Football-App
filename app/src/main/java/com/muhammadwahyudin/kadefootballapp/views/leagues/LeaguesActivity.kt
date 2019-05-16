package com.muhammadwahyudin.kadefootballapp.views.leagues

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.muhammadwahyudin.kadefootballapp.data.Repository
import com.muhammadwahyudin.kadefootballapp.data.remote.TheSportDbApiService
import com.muhammadwahyudin.kadefootballapp.views.leaguedetail.LeagueDetailActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class LeaguesActivity : AppCompatActivity() {

    // Should be injected / or use viewmodel, tapi pengecualian
    // TODO ambil dari DI / Kodein
    private val repository = Repository(TheSportDbApiService.create())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val leagues = repository.getLeagues(this)

        verticalLayout {
            background = ColorDrawable(Color.parseColor("#efefef"))
            recyclerView {
                padding = dip(8)
                lparams(matchParent, matchParent)
                adapter = LeagueAdapter(leagues) { leagueModel ->
                    startActivity(
                        intentFor<LeagueDetailActivity>(
                            LeagueDetailActivity.LEAGUE_PARCEL to leagueModel
                        )
                    )
                    toast("ID ${leagueModel.id}")
                }
                layoutManager = GridLayoutManager(context, 2)
            }
        }
    }

}
