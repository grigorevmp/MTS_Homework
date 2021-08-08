package com.mikhailgrigorev.mts_home.mvvm

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikhailgrigorev.mts_home.MoviesFragment
import com.mikhailgrigorev.mts_home.movieData.Movie
import com.mikhailgrigorev.mts_home.movieData.MoviesModel

typealias MyViewState = MoviesFragment.ViewState

class MvvmViewModel : ViewModel() {

    private val model = MoviesModel()

    val viewState: LiveData<MyViewState> get() = _viewState
    private val _viewState = MutableLiveData<MyViewState>()

    val dataList: LiveData<List<Movie>> get() = _dataList
    private val _dataList = MutableLiveData<List<Movie>>()

    val currentMovie: LiveData<Movie> get() = _currentMovie
    private val _currentMovie = MutableLiveData<Movie>()

    fun loadMovies() {
        model.loadMovies(object : MoviesModel.LoadMovieCallback {
            override fun onLoad(movies: List<Movie>?) {
                _dataList.postValue(movies)
                _viewState.postValue(MyViewState(isDownloaded = false))
            }
        })
    }

    fun loadMovie(id: Long) {
        model.loadMovie(object : MoviesModel.LoadMovieByIdCallback {
            override fun onLoad(movie: Movie?) {
                _currentMovie.postValue(movie)
                _viewState.postValue(MyViewState(isDownloaded = false))
            }
        }, id)
    }

    fun add(userData: Movie) {
    }

    fun clear(context: Context) {
        _viewState.postValue(MyViewState(isDownloaded = true))
        model.clearMovies(object : MoviesModel.CompleteCallback {
            override fun onComplete() {
                loadMovies()
            }
        })
    }
}