package com.mikhailgrigorev.mts_home.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikhailgrigorev.mts_home.MoviesFragment
import com.mikhailgrigorev.mts_home.movieData.Movie
import com.mikhailgrigorev.mts_home.movieData.MoviesModel

typealias MoviesFragmentViewState = MoviesFragment.ViewState

class MoviesViewModel : ViewModel() {

    private val model = MoviesModel()

    val viewState: LiveData<MoviesFragmentViewState> get() = _viewState
    private val _viewState = MutableLiveData<MoviesFragmentViewState>()

    val dataList: LiveData<List<Movie>> get() = _dataList
    private val _dataList = MutableLiveData<List<Movie>>()


    fun loadMovies() {
        model.loadMovies(object : MoviesModel.LoadMovieCallback {
            override fun onLoad(movies: List<Movie>?) {
                if (movies != null) {
                    if (movies.isEmpty()){
                        _viewState.postValue(MoviesFragmentViewState(isDownloading = true))
                    }
                    else{
                        _dataList.postValue(movies)
                        _viewState.postValue(MoviesFragmentViewState(isDownloading = false))
                    }
                }
                else
                    _viewState.postValue(MoviesFragmentViewState(isDownloading = true))
            }
        })
    }


    fun add(userData: Movie) {
    }

    fun clear() {
        _viewState.postValue(MoviesFragmentViewState(isDownloading = true))
        model.clearMovies(object : MoviesModel.CompleteCallback {
            override fun onComplete() {
                loadMovies()
            }
        })
    }
}