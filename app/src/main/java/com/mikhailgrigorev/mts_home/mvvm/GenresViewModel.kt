package com.mikhailgrigorev.mts_home.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mikhailgrigorev.mts_home.App
import com.mikhailgrigorev.mts_home.MoviesFragment
import com.mikhailgrigorev.mts_home.api.GenresResponse
import com.mikhailgrigorev.mts_home.genreData.Genre
import com.mikhailgrigorev.mts_home.genreData.GenreRepository
import retrofit2.Call
import retrofit2.Response

typealias GenresFragmentViewState = MoviesFragment.ViewState

class GenresViewModel : ViewModel() {

    val viewState: LiveData<GenresFragmentViewState> get() = _viewState
    private val _viewState = MutableLiveData<GenresFragmentViewState>()

    val dataList: LiveData<List<Genre>> get() = _dataList
    private val _dataList = MutableLiveData<List<Genre>>()

    private val genreRepository = GenreRepository()

    fun loadGenres() {
        App.instance.apiService.getGenres()
            .enqueue(object : retrofit2.Callback<GenresResponse> {
                override fun onResponse(
                    call: Call<GenresResponse>,
                    response: Response<GenresResponse>
                ) {

                    val genres: MutableList<Genre> = arrayListOf()


                    val genresResponse = response.body()!!.genres
                    for (genre in genresResponse) {
                        genres.add(Genre(genre.id, genre.genre))
                    }


                    genreRepository.insertAll(genres)
                    _dataList.postValue(genres)
                    _viewState.postValue(MoviesFragmentViewState(isDownloading = false))

                }

                override fun onFailure(call: Call<GenresResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

    fun add(genre: Genre) {
    }

    fun clear() {
        _viewState.postValue(MoviesFragmentViewState(isDownloading = true))
    }
}