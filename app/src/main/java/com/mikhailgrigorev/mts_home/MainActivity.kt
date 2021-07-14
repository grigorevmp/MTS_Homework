package com.mikhailgrigorev.mts_home

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikhailgrigorev.mts_home.genreData.GenreDataSourceImpl
import com.mikhailgrigorev.mts_home.genreData.GenreModel
import com.mikhailgrigorev.mts_home.movieData.*

private const val MOVIES_INITIAL_POSITION = 0

class MainActivity : AppCompatActivity(), OnItemClickListener, OnGenreItemClickListener {

    private lateinit var moviesModel: MoviesModel
    private lateinit var genreModel: GenreModel
    private lateinit var adapter: MovieInfoAdapter
    private lateinit var adapterGenre: GenreAdapter
    private var state: Parcelable? = null
    private var state2: Parcelable? = null
    private lateinit var recycler: RecyclerView
    private lateinit var recyclerGenre: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler = findViewById(R.id.moviesList)
        val recyclerEmpty= findViewById<TextView>(R.id.emptyMoviesList)
        recyclerGenre = findViewById(R.id.genreList)

        initDataSource()

        adapter = MovieInfoAdapter(this, moviesModel.getMovies(), this)

        adapterGenre = GenreAdapter(this, genreModel.getGenre(), this)

        recycler.adapter = adapter
        recyclerGenre.adapter = adapterGenre

        recycler.addItemDecoration( RecyclerViewDecoration(24, 0))
        recyclerGenre.addItemDecoration( RecyclerViewDecoration(0, 6))

        if (adapter.itemCount == 0){
            recycler.visibility = View.GONE
            recyclerEmpty.visibility = View.VISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        state = recycler.layoutManager!!.onSaveInstanceState()
        state2 = recyclerGenre.layoutManager!!.onSaveInstanceState()
    }

    override fun onResume() {
        super.onResume()
        if (state != null)
            recycler.layoutManager!!.onRestoreInstanceState(state)
        if (state2 != null)
            recyclerGenre.layoutManager!!.onRestoreInstanceState(state2)


    }

    private fun initDataSource() {
        moviesModel = MoviesModel(MoviesDataSourceImpl())
        genreModel = GenreModel(GenreDataSourceImpl())
    }


    private fun getToastMessage(title: String) =
        getString(R.string.main_click_message, title)

    private fun getGenreToastMessage(title: String) =
        getString(R.string.genre_click_message, title)


    override fun onItemClick(movieTitle: String) {
        showToast(getToastMessage(movieTitle))
    }

    private fun showToast(message: String?) {
        when {
            message.isNullOrEmpty() -> { showToast(getString(R.string.main_empty_message)) }
            else -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onGenreItemClick(genre: String) {
        showToast(getGenreToastMessage(genre))
    }


    fun onMoviesChanged (movies: List<MovieData>){
        val callback = MovieCallback(adapter.movies, movies)
        val diff = DiffUtil.calculateDiff(callback)
        adapter.movies = movies
        diff.dispatchUpdatesTo(adapter)

    }


}