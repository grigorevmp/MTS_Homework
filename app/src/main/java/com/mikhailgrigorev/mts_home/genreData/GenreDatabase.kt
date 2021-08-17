package com.mikhailgrigorev.mts_home.genreData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Genre::class], version = 1)
abstract class GenreDatabase : RoomDatabase(){
    abstract fun genreDao(): GenreDao?

    companion object {

        private var instance: GenreDatabase? = null
        private const val DATABASE_NAME = "Genre.db"

        @Synchronized
        fun getInstance(context: Context): GenreDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(context.applicationContext, GenreDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }

        private val roomCallback = object : Callback() {
        }


    }

}

