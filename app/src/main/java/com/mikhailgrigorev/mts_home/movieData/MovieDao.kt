package com.mikhailgrigorev.mts_home.movieData

import androidx.room.*


@Dao
interface MovieDao {
    @Query("SELECT * FROM movie")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getById(id: Int): Movie?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(films: List<Movie>)

    @Update
    fun update(movie: Movie?)

    @Query("UPDATE movie SET actors_names = :actors_names, actors_paths = :actors_paths, genre_ids = :genre_ids, age_restriction = :age_restriction  WHERE id =:id")
    fun updateSpecial(actors_names: String,
                      actors_paths: String,
                      genre_ids: String,
                      age_restriction: Int,
                      id: Int)

    @Delete
    fun delete(movie: Movie?)

    @Query("DELETE FROM movie WHERE id = :id")
    fun deleteById(id: Long)

    @Query("DELETE FROM movie")
    fun deleteAll()

}