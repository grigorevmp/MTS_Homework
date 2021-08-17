package com.mikhailgrigorev.mts_home.api

import com.mikhailgrigorev.mts_home.api.ApiUtils.Constants.API_KEY
import com.mikhailgrigorev.mts_home.api.ApiUtils.Constants.BASE_URL
import com.mikhailgrigorev.mts_home.api.ApiUtils.addJsonConverter
import com.mikhailgrigorev.mts_home.api.ApiUtils.setClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @GET("discover/movie")
    fun getMovies(
        @Query("sort_by") sort_by: String = "popularity.desc",
        @Query("api_key") api_key: String = API_KEY
    ): Call<ObjectResponse>


    @GET("movie/{id}")
    fun getMovieById(
        @Path("id") id: String,
        @Query("api_key") api_key: String = API_KEY
    ): Call<MovieOneResponse>

    @GET("movie/{id}")
    fun getMovieByIdWithActors(
        @Path("id") id: String,
        @Query("api_key") api_key: String = API_KEY,
        @Query("append_to_response") append_to_response: String = "credits"
    ): Call<MovieWithActorsResponse>

    @GET("genre/movie/list")
    fun getGenres(
        @Query("api_key") api_key: String = API_KEY
    ): Call<GenresResponse>


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
