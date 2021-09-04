package com.mikhailgrigorev.mts_home.mvvm

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikhailgrigorev.mts_home.MoviesFragment
import com.mikhailgrigorev.mts_home.movieData.Movie
import com.mikhailgrigorev.mts_home.movieData.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

typealias MoviesFragmentViewState = MoviesFragment.ViewState

class MoviesViewModel : ViewModel() {


    val viewState: LiveData<MoviesFragmentViewState> get() = _viewState
    private val _viewState = MutableLiveData<MoviesFragmentViewState>()

    val dataList: LiveData<List<Movie>> get() = _dataList
    private val _dataList = MutableLiveData<List<Movie>>()

    private val movieRepository = MovieRepository()

    fun loadMovies() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val moviesForDb: MutableList<Movie> =
                    movieRepository.loadAndInsertAll() as MutableList<Movie>
                _dataList.postValue(moviesForDb)
                _viewState.postValue(MoviesFragmentViewState(isDownloading = false))
            } catch (e: Exception) {
                // handler error
            }
        }
    }

    fun add(userData: Movie) {
    }

    fun clear(context: Context) {
        _viewState.postValue(MoviesFragmentViewState(isDownloading = true))
    }
}