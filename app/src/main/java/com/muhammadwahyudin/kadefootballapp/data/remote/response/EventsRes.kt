package com.muhammadwahyudin.kadefootballapp.data.remote.response

import com.google.gson.annotations.SerializedName
import com.muhammadwahyudin.kadefootballapp.data.model.EventWithImage

data class EventsRes(
    @SerializedName("events")
    val events: List<EventWithImage>?
)