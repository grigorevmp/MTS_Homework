package com.mikhailgrigorev.mts_home.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikhailgrigorev.mts_home.MoviesFragment
import com.mikhailgrigorev.mts_home.movieData.MovieData
import com.mikhailgrigorev.mts_home.movieData.MoviesModel

typealias MyViewState = MoviesFragment.ViewState

class MvvmViewModel : ViewModel() {

    private val model = MoviesModel()

    val viewState: LiveData<MyViewState> get() = _viewState
    private val _viewState = MutableLiveData<MyViewState>()

    val dataList: LiveData<List<MovieData>> get() = _dataList
    private val _dataList = MutableLiveData<List<MovieData>>()

    val currentMovie: LiveData<MovieData> get() = _currentMovie
    private val _currentMovie = MutableLiveData<MovieData>()

    fun loadMovies() {
        model.loadMovies(object : MoviesModel.LoadMovieCallback {
            override fun onLoad(movies: List<MovieData>?) {
                _dataList.postValue(movies)
                _viewState.postValue(MyViewState(isDownloaded = false))
            }
        })
    }

    fun loadMovie(id: Int) {
        model.loadMovie(object : MoviesModel.LoadMovieByIdCallback {
            override fun onLoad(movie: MovieData?) {
                _currentMovie.postValue(movie)
                _viewState.postValue(MyViewState(isDownloaded = false))
            }
        }, id)
    }

    fun add(userData: MovieData) {
    }

    fun clear() {
        _viewState.postValue(MyViewState(isDownloaded = true))
        model.clearMovies(object : MoviesModel.CompleteCallback {
            override fun onComplete() {
                loadMovies()
            }
        })
    }
}