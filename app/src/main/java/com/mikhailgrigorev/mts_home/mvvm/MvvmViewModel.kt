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


    fun loadMovies() {
        model.loadMovies(object : MoviesModel.LoadUserCallback {
            override fun onLoad(movies: List<MovieData>?) {
                _dataList.postValue(movies)
                _viewState.postValue(MyViewState(isDownloaded = false))
            }
        })
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