package com.mikhailgrigorev.mts_home

import android.app.Application
import com.mikhailgrigorev.mts_home.movieData.MovieDatabase
import com.mikhailgrigorev.mts_home.userData.UserDatabase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MovieDatabase.setInstance(this)
        UserDatabase.setInstance(this)
    }

    init {
        instance = this
    }


    companion object {
        lateinit var instance: App
            private set
    }

}