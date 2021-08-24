package com.mikhailgrigorev.mts_home.actorData

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Actor::class], version = 1)
abstract class ActorDatabase : RoomDatabase(){
    abstract fun actorDao(): ActorDao?

    companion object {

        private var instance: ActorDatabase? = null
        private const val DATABASE_NAME = "Actor.db"

        @Synchronized
        fun getInstance(): ActorDatabase {
            return instance!!

        }

        fun setInstance(context: Context): ActorDatabase? {
            if (instance == null) {
                CoroutineScope(Dispatchers.IO).launch {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            ActorDatabase::class.java, DATABASE_NAME
                        ).build()
                    }
                }
            }
            return instance
        }

        private val roomCallback = object : Callback() {
        }


    }

}
