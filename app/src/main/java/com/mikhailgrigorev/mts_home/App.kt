package com.mikhailgrigorev.mts_home

import android.app.Application
import com.mikhailgrigorev.mts_home.api.ApiService

class App : Application() {

    init {
        instance = this
    }

    val apiService: ApiService by lazy { ApiService.create() }

    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        lateinit var instance: App
            private set
    }
}