package com.mikhailgrigorev.mts_home.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikhailgrigorev.mts_home.App
import com.mikhailgrigorev.mts_home.MovieResponse.MoviesModel
import com.mikhailgrigorev.mts_home.MoviesFragment
import com.mikhailgrigorev.mts_home.api.MovieResponse
import com.mikhailgrigorev.mts_home.api.ObjectResponse
import com.mikhailgrigorev.mts_home.movieData.MovieData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

typealias MoviesFragmentViewState = MoviesFragment.ViewState

class MoviesViewModel : ViewModel() {

    private val model = MoviesModel()

    val viewState: LiveData<MoviesFragmentViewState> get() = _viewState
    private val _viewState = MutableLiveData<MoviesFragmentViewState>()

    val dataList: LiveData<List<MovieResponse>> get() = _dataList
    private val _dataList = MutableLiveData<List<MovieResponse>>()


    fun loadMovies() {
        App.instance.apiService.getMovies().enqueue(object : Callback<ObjectResponse> {
            override fun onResponse(
                call: Call<ObjectResponse>,
                response: Response<ObjectResponse>
            ) {
                val movies = response.body()?.results ?: emptyList()
                _dataList.postValue(movies)
                _viewState.postValue(MoviesFragmentViewState(isDownloaded = false))
            }

            override fun onFailure(call: Call<ObjectResponse>, t: Throwable) {
                _dataList.postValue(null)
                _viewState.postValue(MoviesFragmentViewState(isDownloaded = false))
            }
        })
    }

    fun add(userData: MovieData) {
    }

    fun clear() {
        _viewState.postValue(MoviesFragmentViewState(isDownloaded = true))
        model.clearMovies(object : MoviesModel.CompleteCallback {
            override fun onComplete() {
                loadMovies()
            }
        })
    }
}