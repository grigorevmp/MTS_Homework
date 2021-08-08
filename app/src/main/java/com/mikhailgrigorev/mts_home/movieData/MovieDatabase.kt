package com.mikhailgrigorev.mts_home.movieData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


@Database(entities = [Movie::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao?

    companion object {

        private var instance: MovieDatabase? = null
        private const val DATABASE_NAME = "Movie.db"

        @Synchronized
        fun getInstance(context: Context): MovieDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(context.applicationContext, MovieDatabase::class.java,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instance!!)
            }
        }

        private fun populateDatabase(db: MovieDatabase) {
            val movieDao = db.movieDao()
            CoroutineScope(IO).launch {
                movieDao!!.insertAll(MovieTestData.getMovies())
            }
        }


    }

}

