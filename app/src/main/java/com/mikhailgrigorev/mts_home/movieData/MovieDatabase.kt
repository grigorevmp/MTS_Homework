package com.mikhailgrigorev.mts_home.movieData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import androidx.room.migration.Migration
import kotlinx.coroutines.Dispatchers


@Database(entities = [Movie::class], version = 2)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao?

    companion object {

        private val MIGRATION_Movie_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE movie ADD COLUMN actors TEXT DEFAULT '0' NOT NULL")
            }
        }

        private var instance: MovieDatabase? = null
        private const val DATABASE_NAME = "Movie.db"

        @Synchronized
        fun getInstance(): MovieDatabase {
            return instance!!

        }

        fun setInstance(context: Context): MovieDatabase? {
            if (instance == null) {
                CoroutineScope(IO).launch {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            MovieDatabase::class.java, DATABASE_NAME
                        ).build()
                    }
                    populateDatabase(instance!!)
                }
            }
            return instance
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

