package com.mikhailgrigorev.mts_home.mvvm

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikhailgrigorev.mts_home.MoviesDetailFragment
import com.mikhailgrigorev.mts_home.movieData.Movie
import com.mikhailgrigorev.mts_home.movieData.MoviesModel

typealias MoviesDetailsFragmentViewState = MoviesDetailFragment.ViewState

class MovieCardViewModel : ViewModel() {

    private val model = MoviesModel()

    val viewState: LiveData<MoviesDetailsFragmentViewState> get() = _viewState
    private val _viewState = MutableLiveData<MoviesDetailsFragmentViewState>()

    val currentMovie: LiveData<Movie> get() = _currentMovie
    private val _currentMovie = MutableLiveData<Movie>()

    fun loadMovie(id: Long) {
        model.loadMovie(object : MoviesModel.LoadMovieByIdCallback {
            override fun onLoad(movie: Movie?) {
                _currentMovie.postValue(movie)
                _viewState.postValue(MoviesDetailsFragmentViewState(isDownloaded = false))
            }
        }, id)
    }

    fun add(userData: Movie) {
    }

    fun clear(context: Context) {
    }
}

