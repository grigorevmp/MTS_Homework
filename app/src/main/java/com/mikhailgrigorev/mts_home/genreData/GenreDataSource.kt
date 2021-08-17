package com.mikhailgrigorev.mts_home.genreData

import com.mikhailgrigorev.mts_home.api.GenreResponse

interface GenreDataSource {
    fun getGenres(): List<GenreResponse>
}