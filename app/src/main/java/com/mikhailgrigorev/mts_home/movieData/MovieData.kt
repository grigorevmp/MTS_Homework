package com.mikhailgrigorev.mts_home.movieData

import java.io.Serializable
import java.util.ArrayList

data class MovieData(
    val id: Int,
    val title: String,
    val description: String,
    val rateScore: Int = 0,
    val ageRestriction: Int,
    val imageUrl: String
)

