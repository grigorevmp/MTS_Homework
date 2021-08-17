package com.mikhailgrigorev.mts_home.userData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null,
    @ColumnInfo(name = "login")
    val login: String,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "passwordHash")
    val passwordHash: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "phone")
    val phone: String,
)
