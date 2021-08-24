package com.mikhailgrigorev.mts_home.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikhailgrigorev.mts_home.App
import com.mikhailgrigorev.mts_home.MoviesFragment
import com.mikhailgrigorev.mts_home.api.GenresResponse
import com.mikhailgrigorev.mts_home.genreData.Genre
import com.mikhailgrigorev.mts_home.genreData.GenreModel
import com.mikhailgrigorev.mts_home.genreData.GenreRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

typealias GenresFragmentViewState = MoviesFragment.ViewState

class GenresViewModel : ViewModel() {

    private val model = GenreModel()

    val viewState: LiveData<GenresFragmentViewState> get() = _viewState
    private val _viewState = MutableLiveData<GenresFragmentViewState>()

    val dataList: LiveData<List<Genre>> get() = _dataList
    private val _dataList = MutableLiveData<List<Genre>>()


    fun loadMovies() {
        model.loadGenres(object : GenreModel.LoadGenreCallback {
            override fun onLoad(genres: List<Genre>?) {
                if (genres != null){
                    if (genres.isNotEmpty()){
                        _dataList.postValue(genres)
                        _viewState.postValue(MoviesFragmentViewState(isDownloading = false))
                    }
                    else
                        _viewState.postValue(MoviesFragmentViewState(isDownloading = true))
                }
                else
                    _viewState.postValue(MoviesFragmentViewState(isDownloading = true))


            }
        })



    }

    fun add(genre: Genre) {
    }

    fun clear() {
        _viewState.postValue(MoviesFragmentViewState(isDownloading = true))
        model.clearMovies(object : GenreModel.CompleteCallback {
            override fun onComplete() {
                loadMovies()
            }
        })
    }
}