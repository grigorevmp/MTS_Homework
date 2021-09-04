package com.mikhailgrigorev.mts_home.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikhailgrigorev.mts_home.MoviesFragment
import com.mikhailgrigorev.mts_home.genreData.Genre
import com.mikhailgrigorev.mts_home.genreData.GenreRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

typealias GenresFragmentViewState = MoviesFragment.ViewState

class GenresViewModel : ViewModel() {

    val viewState: LiveData<GenresFragmentViewState> get() = _viewState
    private val _viewState = MutableLiveData<GenresFragmentViewState>()

    val dataList: LiveData<List<Genre>> get() = _dataList
    private val _dataList = MutableLiveData<List<Genre>>()

    private val genreRepository = GenreRepository()

    fun loadGenres() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val genres: MutableList<Genre> =
                    genreRepository.loadAndInsertAll() as MutableList<Genre>
                _dataList.postValue(genres)
                _viewState.postValue(MoviesFragmentViewState(isDownloading = false))

            } catch (e: Exception) {
                // handler error
            }
        }
    }

    fun add(genre: Genre) {
    }

    fun clear() {
        _viewState.postValue(MoviesFragmentViewState(isDownloading = true))
    }
}