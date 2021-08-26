package com.mikhailgrigorev.mts_home.userData

import androidx.room.*


@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getById(id: Long): User?

    @Query("SELECT * FROM user WHERE login = :login")
    fun getByLogin(login: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User?)

    @Insert
    fun insertAll(users: List<User>)

    @Update
    fun update(v: User?)

    @Delete
    fun delete(user: User?)

    @Query("DELETE FROM user WHERE id = :id")
    fun deleteById(id: Long)

    @Query("DELETE FROM user")
    fun deleteAll()

}