package com.mikhailgrigorev.mts_home.genreData

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GenreRepository {

    private var genreDao: GenreDao

    private val database = GenreDatabase.getInstance()

    init {
        genreDao = database.genreDao()!!
    }

    fun insert(genre: Genre) {
        CoroutineScope(Dispatchers.IO).launch {
            genreDao.insert(genre)
        }
    }

    fun insertAll(genres: List<Genre>) {
        CoroutineScope(Dispatchers.IO).launch {
            genreDao.insertAll(genres)
        }
    }

    fun update(genre: Genre) {
        CoroutineScope(Dispatchers.IO).launch {
            genreDao.update(genre)
        }
    }

    fun delete(genre: Genre) {
        CoroutineScope(Dispatchers.IO).launch {
            genreDao.delete(genre)
        }
    }

    fun deleteAllGenres() {
        CoroutineScope(Dispatchers.IO).launch {
            genreDao.deleteAll()
        }
    }

    fun getAllGenres(): List<Genre> {
        return genreDao.getAll()
    }

    fun getGenreById(id: Int): Genre {
        return genreDao.getById(id)!!
    }



}