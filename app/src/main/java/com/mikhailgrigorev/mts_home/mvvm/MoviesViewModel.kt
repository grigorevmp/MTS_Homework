package com.mikhailgrigorev.mts_home.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikhailgrigorev.mts_home.MovieResponse.MoviesModel
import com.mikhailgrigorev.mts_home.MoviesFragment
import com.mikhailgrigorev.mts_home.api.MovieResponse
import com.mikhailgrigorev.mts_home.movieData.MovieData

typealias MoviesFragmentViewState = MoviesFragment.ViewState

class MoviesViewModel : ViewModel() {

    private val model = MoviesModel()

    val viewState: LiveData<MoviesFragmentViewState> get() = _viewState
    private val _viewState = MutableLiveData<MoviesFragmentViewState>()

    val dataList: LiveData<List<MovieResponse>> get() = _dataList
    private val _dataList = MutableLiveData<List<MovieResponse>>()


    fun loadMovies() {
        model.loadMovies(object : MoviesModel.LoadMovieCallback {
            override fun onLoad(movies: List<MovieResponse>?) {
                _dataList.postValue(movies)
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