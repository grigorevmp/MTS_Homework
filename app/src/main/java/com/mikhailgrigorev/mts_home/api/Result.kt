package com.mikhailgrigorev.mts_home.api

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.ArrayList


@Keep
@Serializable
data class ObjectResponse(
    @SerialName("results") val results: List<MovieResponse>
    )

@Keep
@Serializable
data class MovieResponse(
    @SerialName("id") val id: Int = 0,
    @SerialName("title") val title: String = "null",
    @SerialName("poster_path") val poster_path: String = "null",
    @SerialName("overview") val overview: String = "null",
    @SerialName("vote_average") val vote_average: Int = 0,
    @SerialName("ageRestriction") val ageRestriction: Int = 0,
    @SerialName("genreIds") val genreIds: ArrayList<Int> = arrayListOf(),



    @SerialName("adult") val adult: Boolean = false,
    @SerialName("backdrop_path") val backdrop_path: String = "null",
    @SerialName("original_language") val original_language: String = "null",
    @SerialName("video") val video: Boolean  = false,
    @SerialName("popularity") val popularity: Float = 0.9F,
    @SerialName("vote_count") val vote_count: Int = 0,
    @SerialName("release_date") val release_date: String = "null",


    )

