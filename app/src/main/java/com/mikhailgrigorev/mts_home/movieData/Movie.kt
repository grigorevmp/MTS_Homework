package com.mikhailgrigorev.mts_home.movieData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mikhailgrigorev.mts_home.api.MovieResponse

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String = "null",
    @ColumnInfo(name = "poster_path")
    val poster_path: String = "null",
    @ColumnInfo(name = "overview")
    val overview: String = "null",
    @ColumnInfo(name = "vote_average")
    val vote_average: Float = 0.0F,
    @ColumnInfo(name = "genre_ids")
    var genre_ids: String,
    @ColumnInfo(name = "actors_ids")
    var actors_ids: String = "",
    @ColumnInfo(name = "actors_names")
    var actors_names: String = "",
    @ColumnInfo(name = "actors_paths")
    var actors_paths: String = "",
    @ColumnInfo(name = "adult")
    val adult: Boolean = false,
    @ColumnInfo(name = "backdrop_path")
    val backdrop_path: String = "null",
    @ColumnInfo(name = "original_language")
    val original_language: String = "null",
    @ColumnInfo(name = "video")
    val video: Boolean  = false,
    @ColumnInfo(name = "popularity")
    val popularity: Float = 0.9F,
    @ColumnInfo(name = "vote_count")
    val vote_count: Int = 0,
    @ColumnInfo(name = "release_date")
    val release_date: String = "null",
    @ColumnInfo(name = "age_restriction")
    var age_restriction: Int = 0
) {
    companion object {
        fun movieFromResponse(movie: MovieResponse) = Movie(
            id = movie.id,
            title = movie.title,
            poster_path = movie.poster_path,
            overview = movie.overview,
            vote_average = movie.vote_average,
            genre_ids = movie.genre_ids.joinToString(' '.toString()),
            adult = movie.adult,
            backdrop_path = movie.backdrop_path,
            original_language = movie.original_language,
            video = movie.video,
            popularity = movie.popularity,
            vote_count = movie.vote_count,
            release_date = movie.release_date
        )
    }
}