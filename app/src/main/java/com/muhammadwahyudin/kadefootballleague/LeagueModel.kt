package com.muhammadwahyudin.kadefootballleague

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LeagueModel(val name: String, val id: Int, val description: String, val image: Int) : Parcelable