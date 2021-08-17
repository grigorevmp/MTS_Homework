package com.mikhailgrigorev.mts_home.userData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao?

    companion object {

        private var instance: UserDatabase? = null
        private const val DATABASE_NAME = "User.db"

        @Synchronized
        fun getInstance(context: Context): UserDatabase {
            if (instance == null)
                instance = Room.databaseBuilder(
                    context.applicationContext, UserDatabase::class.java,
                    DATABASE_NAME
                )
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

        private fun populateDatabase(db: UserDatabase) {
            val userDao = db.userDao()
            CoroutineScope(Dispatchers.IO).launch {
                userDao!!.insert(
                    User(
                        login = "Mikhail",
                        email = "grigorevmp@gmail.com",
                        name = "Mikhail",
                        passwordHash = "81dc9bdb52d04dc20036dbd8313ed055",
                        phone = "89509073454"
                    )
                )
            }
        }


    }

}

