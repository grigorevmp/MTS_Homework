package com.mikhailgrigorev.mts_home.genreData

import androidx.room.*
import com.mikhailgrigorev.mts_home.api.MovieWithActorsResponse


@Dao
interface GenreDao {
    @Query("SELECT * FROM genre")
    fun getAll(): List<Genre>

    @Query("SELECT * FROM genre WHERE id = :id")
    fun getById(id: Int): Genre?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Genre?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(genres: List<Genre>)

    @Update
    fun update(v: Genre?)

    @Delete
    fun delete(movie: Genre?)

    @Query("DELETE FROM genre WHERE id = :id")
    fun deleteById(id: Int)

    @Query("DELETE FROM genre")
    fun deleteAll()

}