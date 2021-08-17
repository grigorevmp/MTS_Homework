package com.mikhailgrigorev.mts_home.movieData

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "rate_score")
    val rateScore: Int = 0,
    @ColumnInfo(name = "age_restriction")
    val ageRestriction: Int,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "actors")
    val actors: String = "0"

)
