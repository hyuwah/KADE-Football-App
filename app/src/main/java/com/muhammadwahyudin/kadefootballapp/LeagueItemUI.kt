package com.muhammadwahyudin.kadefootballapp

import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView

class LeagueItemUI : AnkoComponent<ViewGroup> {

    override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
        cardView {
            radius = dip(8).toFloat()
            lparams(wrapContent, wrapContent) {
                margin = dip(10)
            }
            verticalLayout {
                lparams(matchParent, matchParent)
                imageView {
                    id = R.id.league_item_image
                    adjustViewBounds = true
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }.lparams(width = matchParent)

                textView("League Name") {
                    padding = dip(8)
                    id = R.id.league_item_name
                    textAppearance = R.style.TextAppearance_MaterialComponents_Subtitle1
                    textColor = Color.BLACK
                    gravity = Gravity.CENTER
                }.lparams(
                    width = matchParent
                )
            }
        }
    }


}