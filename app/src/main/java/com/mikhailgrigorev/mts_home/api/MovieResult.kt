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
data class GenreResponse(
    @SerialName("id") val id: Int = 0,
    @SerialName("name") val genre: String = "null"
)

@Keep
@Serializable
data class MovieResponse(
    @SerialName("id") val id: Int = 0,
    @SerialName("title") val title: String = "null",
    @SerialName("poster_path") val poster_path: String = "null",
    @SerialName("overview") val overview: String = "null",
    @SerialName("vote_average") val vote_average: Float = 0.0F,
    @SerialName("genre_ids") val genre_ids: ArrayList<Int> = arrayListOf(),
    @SerialName("adult") val adult: Boolean = false,
    @SerialName("backdrop_path") val backdrop_path: String = "null",
    @SerialName("original_language") val original_language: String = "null",
    @SerialName("video") val video: Boolean  = false,
    @SerialName("popularity") val popularity: Float = 0.9F,
    @SerialName("vote_count") val vote_count: Int = 0,
    @SerialName("release_date") val release_date: String = "null"
)


@Keep
@Serializable
data class ActorResponse(
    @SerialName("id") val id: Int = 0,
    @SerialName("name") val name: String = "null",
    @SerialName("profile_path") val profile_path: String = "null"
)

@Keep
@Serializable
data class CreditsResponse(
    @SerialName("cast") val cast: ArrayList<ActorResponse> = arrayListOf(),
)


@Keep
@Serializable
data class MovieOneResponse(
    @SerialName("id") val id: Int = 0,
    @SerialName("title") val title: String = "null",
    @SerialName("poster_path") val poster_path: String = "null",
    @SerialName("overview") val overview: String = "null",
    @SerialName("vote_average") val vote_average: Float = 0.0F,
    @SerialName("ageRestriction") val ageRestriction: Int = 0,
    @SerialName("genres") val genres: ArrayList<GenreResponse> = arrayListOf(),
    @SerialName("adult") val adult: Boolean = false,
    @SerialName("backdrop_path") val backdrop_path: String = "null",
    @SerialName("original_language") val original_language: String = "null",
    @SerialName("video") val video: Boolean  = false,
    @SerialName("popularity") val popularity: Float = 0.9F,
    @SerialName("vote_count") val vote_count: Int = 0,
    @SerialName("release_date") val release_date: String = "null"
)


@Keep
@Serializable
data class MovieWithActorsResponse(
    @SerialName("id") val id: Int = 0,
    @SerialName("title") val title: String = "null",
    @SerialName("poster_path") val poster_path: String = "null",
    @SerialName("overview") val overview: String = "null",
    @SerialName("vote_average") val vote_average: Float = 0.0F,
    @SerialName("ageRestriction") val ageRestriction: Int = 0,
    @SerialName("genres") val genres: ArrayList<GenreResponse> = arrayListOf(),
    @SerialName("credits") val credits: CreditsResponse,
    @SerialName("adult") val adult: Boolean = false,
    @SerialName("backdrop_path") val backdrop_path: String = "null",
    @SerialName("original_language") val original_language: String = "null",
    @SerialName("video") val video: Boolean  = false,
    @SerialName("popularity") val popularity: Float = 0.9F,
    @SerialName("vote_count") val vote_count: Int = 0,
    @SerialName("release_date") val release_date: String = "null"
)

