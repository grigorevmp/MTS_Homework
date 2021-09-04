package com.mikhailgrigorev.mts_home.actorData

import androidx.room.*

@Dao
interface ActorDao {
    @Query("SELECT * FROM actor")
    fun getAll(): List<Actor>

    @Query("SELECT * FROM actor WHERE id = :id")
    fun getById(id: Int): Actor?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(actor: Actor?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(actors: List<Actor>)

    @Update
    fun update(v: Actor?)

    @Delete
    fun delete(actor: Actor?)

    @Query("DELETE FROM actor WHERE id = :id")
    fun deleteById(id: Int)

    @Query("DELETE FROM actor")
    fun deleteAll()

}