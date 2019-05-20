package com.muhammadwahyudin.kadefootballapp.app

import android.view.View
import java.text.SimpleDateFormat
import java.util.*

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun String.toReadableTimeWIB(dateString: String): String {
    var time: Date? = null
    if (dateString.contains("-"))
        if (this.contains("+"))
            time = SimpleDateFormat("yyyy-MM-dd HH:mm:ssz").parse("$dateString ${this}")
        else time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("$dateString ${this}")
    else if (dateString.contains("/"))
        if (this.contains("+"))
            time = SimpleDateFormat("yyyy/MM/dd HH:mm:ssz").parse("$dateString ${this}")
        else time = SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("$dateString ${this}")

    if (time == null) return "-"
    return SimpleDateFormat("EEEE, dd MMM yyyy\nHH:mm z", Locale("id", "ID")).format(time)
}
