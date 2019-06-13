package com.muhammadwahyudin.kadefootballapp.views.leagues

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.data.IRepository
import com.muhammadwahyudin.kadefootballapp.views.favoritematch.FavoriteMatchActivity
import com.muhammadwahyudin.kadefootballapp.views.leaguedetail.LeagueDetailActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.koin.android.ext.android.inject

class LeaguesActivity : AppCompatActivity() {

    // Should be injected / or use viewmodel, tapi pengecualian
    private val repository: IRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val leagues = repository.getLeagues(this.resources)

        verticalLayout {
            background = ColorDrawable(Color.parseColor("#efefef"))
            cardView {
                foreground = with(TypedValue()) {
                    context.theme.resolveAttribute(R.attr.selectableItemBackground, this, true)
                    ContextCompat.getDrawable(context, resourceId)
                }
                isClickable = true
                radius = dip(8).toFloat()
                onClick { startActivity(intentFor<FavoriteMatchActivity>()) }
                frameLayout {
                    imageView {
                        setImageResource(R.drawable.favorite_match_bg)
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }.lparams(matchParent, dip(64))
                    imageView {
                        setImageResource(R.drawable.appbar_scrim)
                    }.lparams(matchParent, dip(64))
                    textView("Favorite Matches") {
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                        textSize = 24f
                        textColor = Color.WHITE
                        gravity = Gravity.CENTER
                    }.lparams(matchParent, matchParent)
                }
            }.lparams(matchParent, wrapContent) {
                margin = dip(12)
            }
            recyclerView {
                padding = dip(8)
                lparams(matchParent, matchParent)
                adapter = LeagueAdapter(leagues) { leagueModel ->
                    startActivity(
                        intentFor<LeagueDetailActivity>(
                            LeagueDetailActivity.LEAGUE_PARCEL to leagueModel
                        )
                    )
                }
                layoutManager = GridLayoutManager(context, 2)
            }
        }
    }

}
