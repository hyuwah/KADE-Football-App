package com.muhammadwahyudin.kadefootballapp.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class League(
    val id: Int,
    val name: String,
    val desc: String,
    val logoRes: Int
) : Parcelable