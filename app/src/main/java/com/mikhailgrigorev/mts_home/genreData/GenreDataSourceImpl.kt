package com.mikhailgrigorev.mts_home.genreData

class GenreDataSourceImpl: GenreDataSource {
    override fun getGenres() = listOf(
        GenreData("Боевик"),
        GenreData("Приключения"),
        GenreData("Мультик"),
        GenreData("Комедия"),
        GenreData("Криминал"),
        GenreData("Документальное"),
        GenreData("Драма"),
        GenreData("Семейный"),
        GenreData("Фантастика"),
        GenreData("Исторический"),
        GenreData("Ужасы"),
        GenreData("Музыка"),
        GenreData("Мистика"),
        GenreData("Романтика"),
        GenreData("Научная фантастика"),
        GenreData("Триллер"),
        GenreData("Военный"),
        GenreData("Вестерн"),
        GenreData("Рельное ТВ"),
    )
}
