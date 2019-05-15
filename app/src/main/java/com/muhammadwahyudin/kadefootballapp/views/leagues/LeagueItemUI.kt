package com.muhammadwahyudin.kadefootballapp.views.leagues

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.muhammadwahyudin.kadefootballapp.R
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView

class LeagueItemUI : AnkoComponent<ViewGroup> {

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        cardView {
            isClickable = true
            radius = dip(8).toFloat()
            lparams(matchParent, wrapContent) {
                margin = dip(4)
            }
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                lparams(matchParent, matchParent)
                imageView {
                    padding = dip(8)
                    id = R.id.league_item_image
                    adjustViewBounds = true
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }.lparams(height = 200)

                textView("League Name") {
                    padding = dip(8)
                    id = R.id.league_item_name
                    textColor = Color.BLACK
                    gravity = Gravity.CENTER
                }.lparams(
                    height = matchParent
                )
            }
        }
    }


}