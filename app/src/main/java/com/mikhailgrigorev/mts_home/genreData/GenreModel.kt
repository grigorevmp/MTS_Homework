package com.mikhailgrigorev.mts_home.genreData

class GenreModel(
        private val genresDataSource:GenreDataSourceImpl
) {

    fun getGenre() = genresDataSource.getGenres()
}