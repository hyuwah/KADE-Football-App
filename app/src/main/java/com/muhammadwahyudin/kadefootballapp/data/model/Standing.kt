package com.muhammadwahyudin.kadefootballapp.data.model

data class Standing(
    val draw: Int,
    val goalsagainst: Int,
    val goalsdifference: Int,
    val goalsfor: Int,
    val loss: Int,
    val name: String,
    val played: Int,
    val teamid: String,
    val total: Int,
    val win: Int
)