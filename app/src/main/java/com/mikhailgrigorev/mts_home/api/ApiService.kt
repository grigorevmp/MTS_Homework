package com.mikhailgrigorev.mts_home.api

import com.mikhailgrigorev.mts_home.api.ApiUtils.Constants.API_KEY
import com.mikhailgrigorev.mts_home.api.ApiUtils.Constants.BASE_URL
import com.mikhailgrigorev.mts_home.api.ApiUtils.addJsonConverter
import com.mikhailgrigorev.mts_home.api.ApiUtils.setClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("discover/movie")
    fun getMovies(
        @Query("sort_by") sort_by: String = "popularity",
        @Query("api_key") api_key: String = API_KEY
    ): Call<ObjectResponse>

    //@GET("3/movie/popular")
    //fun getMovies(
        //@Query("api_key") key: String = API_KEY
    //): Call<ObjectResponse>

    @GET("3/movie/popular")
    suspend fun getMoviesCoroutines(
        @Query("api_key") key: String = API_KEY
    ): ObjectResponse

    companion object {
        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .setClient()
                .addJsonConverter()
                .build()
                .create(ApiService::class.java)
        }
    }
}
