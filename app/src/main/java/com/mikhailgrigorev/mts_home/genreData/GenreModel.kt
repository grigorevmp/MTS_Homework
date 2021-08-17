package com.mikhailgrigorev.mts_home.genreData

class GenreModel(
        private val genresDataSource:GenreDataSourceImpl
) {

    fun getGenres() = genresDataSource.getGenres()

    fun getGenre(id: Int): String? {
        for (genre in genresDataSource.getGenres()){
            if (genre.id == id)
                return genre.genre
        }
        return null
    }
}