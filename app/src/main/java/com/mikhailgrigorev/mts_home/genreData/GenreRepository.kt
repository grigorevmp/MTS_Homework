package com.mikhailgrigorev.mts_home.genreData

import com.mikhailgrigorev.mts_home.App
import com.mikhailgrigorev.mts_home.api.GenresResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

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

    suspend fun loadAndInsertAll(): List<Genre> {
        val job = CoroutineScope(Dispatchers.IO).launch {
            App.instance.apiService.getGenres()
                .enqueue(object : retrofit2.Callback<GenresResponse> {
                    override fun onResponse(
                        call: Call<GenresResponse>,
                        response: Response<GenresResponse>
                    ) {

                        val genres: MutableList<Genre> = arrayListOf()
                        val genresResponse = response.body()!!.genres
                        for (genre in genresResponse) {
                            genres.add(Genre(genre.id, genre.genre))
                        }
                        insertAll(genres)

                    }

                    override fun onFailure(call: Call<GenresResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
        }
        job.join()
        return getAllGenres()
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