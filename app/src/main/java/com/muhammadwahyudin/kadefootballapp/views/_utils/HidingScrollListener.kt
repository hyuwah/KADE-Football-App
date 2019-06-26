package com.muhammadwahyudin.kadefootballapp.views._utils

import androidx.recyclerview.widget.RecyclerView

abstract class HidingScrollListener : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING) {
            onHide()
        } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            onShow()
        }
    }

    abstract fun onHide()
    abstract fun onShow()

}