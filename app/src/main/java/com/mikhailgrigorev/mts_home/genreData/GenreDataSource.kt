package com.mikhailgrigorev.mts_home.genreData

interface GenreDataSource {
    fun getGenres(): List<GenreData>
}