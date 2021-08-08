package com.mikhailgrigorev.mts_home.movieData

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface MovieDao {
    @Query("SELECT * FROM movie")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getById(id: Long): Movie?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie?)

    @Insert
    fun insertAll(films: List<Movie>)

    @Update
    fun update(v: Movie?)

    @Delete
    fun delete(movie: Movie?)

    @Query("DELETE FROM movie WHERE id = :id")
    fun deleteById(id: Long)

    @Query("DELETE FROM movie")
    fun deleteAll()

}