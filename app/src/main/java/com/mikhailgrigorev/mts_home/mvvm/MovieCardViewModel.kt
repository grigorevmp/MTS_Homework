package com.mikhailgrigorev.mts_home.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikhailgrigorev.mts_home.MoviesDetailFragment
import com.mikhailgrigorev.mts_home.movieData.MovieData
import com.mikhailgrigorev.mts_home.movieData.MoviesModel

typealias MoviesDetailsFragmentViewState = MoviesDetailFragment.ViewState

class MovieCardViewModel : ViewModel(){

    private val model = MoviesModel()

    val viewState: LiveData<MoviesDetailsFragmentViewState> get() = _viewState
    private val _viewState = MutableLiveData<MoviesDetailsFragmentViewState>()

    val currentMovie: LiveData<MovieData> get() = _currentMovie
    private val _currentMovie = MutableLiveData<MovieData>()


    fun loadMovie(id: Int) {
        model.loadMovie(object : MoviesModel.LoadMovieByIdCallback {
            override fun onLoad(movie: MovieData?) {
                _currentMovie.postValue(movie)
                _viewState.postValue(MoviesDetailsFragmentViewState(isDownloaded = false))
            }
        }, id)
    }

    fun add(userData: MovieData) {
    }

    fun clear() {
    }
}