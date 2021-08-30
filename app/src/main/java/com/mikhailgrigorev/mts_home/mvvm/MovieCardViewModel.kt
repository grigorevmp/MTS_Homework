package com.mikhailgrigorev.mts_home.mvvm

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mikhailgrigorev.mts_home.MoviesDetailFragment
import com.mikhailgrigorev.mts_home.movieData.Movie
import com.mikhailgrigorev.mts_home.movieData.MovieRepository
import kotlinx.coroutines.launch

typealias MoviesDetailsFragmentViewState = MoviesDetailFragment.ViewState

class MovieCardViewModel : ViewModel() {

    val viewState: LiveData<MoviesDetailsFragmentViewState> get() = _viewState
    private val _viewState = MutableLiveData<MoviesDetailsFragmentViewState>()

    val currentMovie: LiveData<Movie> get() = _currentMovie
    private val _currentMovie = MutableLiveData<Movie>()

    private val movieRepository = MovieRepository()

    fun loadMovie(id: Long) {
        viewModelScope.launch {
            try {
                val movie = movieRepository.getMovieById(id)
                _currentMovie.postValue(movie)
                _viewState.postValue(MoviesDetailsFragmentViewState(isDownloaded = false))
            } catch (e: Exception) {
                // handler error
            }
        }
    }

    fun add(userData: Movie) {
    }

    fun clear(context: Context) {
    }
}

