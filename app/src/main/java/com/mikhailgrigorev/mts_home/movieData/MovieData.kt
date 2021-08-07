package com.mikhailgrigorev.mts_home.movieData

data class MovieData(
    val id: Int,
    val title: String,
    val description: String,
    val rateScore: Int = 0,
    val ageRestriction: Int,
    val imageUrl: String
)

