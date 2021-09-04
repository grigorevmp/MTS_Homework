package com.mikhailgrigorev.mts_home.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikhailgrigorev.mts_home.MoviesDetailFragment
import com.mikhailgrigorev.mts_home.api.MovieWithActorsResponse
import com.mikhailgrigorev.mts_home.movieData.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

typealias MoviesDetailsFragmentViewState = MoviesDetailFragment.ViewState

class MovieCardViewModel : ViewModel(){

    val viewState: LiveData<MoviesDetailsFragmentViewState> get() = _viewState
    private val _viewState = MutableLiveData<MoviesDetailsFragmentViewState>()

    val currentMovie: LiveData<MovieWithActorsResponse> get() = _currentMovie
    private val _currentMovie = MutableLiveData<MovieWithActorsResponse>()

    private val movieRepository = MovieRepository()

    fun loadMovie(id: Long) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                movieRepository.loadAndReturn(id)
                _currentMovie.postValue(movieRepository.getMovieTemp())
                _viewState.postValue(MoviesDetailsFragmentViewState(isDownloading = false))

            } catch (e: Exception) {
                print(e.message)
            }
        }
    }

    fun add(userData: MovieWithActorsResponse) {
    }

    fun clear() {
    }
}