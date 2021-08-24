package com.mikhailgrigorev.mts_home.actorData

import androidx.room.*

@Entity(tableName = "actor")
data class Actor(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val actor_id: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "profile_path")
    val profile_path: String

)

