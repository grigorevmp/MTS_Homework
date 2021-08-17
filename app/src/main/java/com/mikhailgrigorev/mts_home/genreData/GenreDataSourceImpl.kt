package com.mikhailgrigorev.mts_home.genreData

import com.mikhailgrigorev.mts_home.api.GenreResponse

class GenreDataSourceImpl : GenreDataSource {
    override fun getGenres() = listOf(
        GenreResponse(28, "Боевик"),
        GenreResponse(12, "Приключения"),
        GenreResponse(16, "Мультик"),
        GenreResponse(35, "Комедия"),
        GenreResponse(80, "Криминал"),
        GenreResponse(99, "Документальное"),
        GenreResponse(18, "Драма"),
        GenreResponse(10751, "Семейный"),
        GenreResponse(14, "Фантастика"),
        GenreResponse(36, "Исторический"),
        GenreResponse(27, "Ужасы"),
        GenreResponse(10402, "Музыка"),
        GenreResponse(9648, "Мистика"),
        GenreResponse(10749, "Романтика"),
        GenreResponse(878, "Научная фантастика"),
        GenreResponse(10770, "Триллер"),
        GenreResponse(53, "Военный"),
        GenreResponse(10752, "Вестерн")
    )
}
