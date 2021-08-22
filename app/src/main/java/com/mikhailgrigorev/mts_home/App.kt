package com.mikhailgrigorev.mts_home

import android.app.Application
import com.mikhailgrigorev.mts_home.api.ApiService
import com.mikhailgrigorev.mts_home.genreData.GenreDatabase
import com.mikhailgrigorev.mts_home.movieData.MovieDatabase
import com.mikhailgrigorev.mts_home.userData.UserDatabase

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        MovieDatabase.setInstance(this)
        GenreDatabase.setInstance(this)
        UserDatabase.setInstance(this)
    }

    init {
        instance = this
    }

    val apiService: ApiService by lazy { ApiService.create() }

    companion object {
        lateinit var instance: App
            private set
    }
}