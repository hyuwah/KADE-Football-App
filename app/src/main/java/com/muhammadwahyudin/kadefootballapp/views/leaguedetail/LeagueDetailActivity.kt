package com.muhammadwahyudin.kadefootballapp.views.leaguedetail

import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.muhammadwahyudin.kadefootballapp.R
import com.muhammadwahyudin.kadefootballapp.data.model.League
import org.jetbrains.anko.*

class LeagueDetailActivity : AppCompatActivity() {

    companion object {
        const val LEAGUE_PARCEL = "league_parcel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val leagueModel = intent.getParcelableExtra<League>(LEAGUE_PARCEL)
        title = leagueModel.name

        scrollView {
            isFillViewport = true
            lparams(matchParent, matchParent)
            verticalLayout {
                lparams(matchParent, wrapContent)
                imageView {
                    adjustViewBounds = true
                    scaleType = ImageView.ScaleType.FIT_CENTER
                    setImageResource(leagueModel.logoRes)
                }.lparams(width = matchParent, height = dip(200)) {
                    bottomMargin = dip(12)
                }

                textView(leagueModel.name) {
                    textAppearance =
                        R.style.TextAppearance_MaterialComponents_Headline5
                    gravity = Gravity.CENTER
                }.lparams(width = matchParent) {
                    topMargin = dip(12)
                    leftMargin = dip(12)
                    rightMargin = dip(12)
                }

                textView(leagueModel.desc) {
                    textAppearance =
                        R.style.TextAppearance_MaterialComponents_Body1
                }.lparams(width = matchParent) {
                    margin = dip(16)
                }
            }
        }
    }
}
