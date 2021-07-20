package com.mikhailgrigorev.mts_home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mikhailgrigorev.mts_home.movieData.MovieData
import com.mikhailgrigorev.mts_home.movieData.MoviesDataSourceImpl
import com.mikhailgrigorev.mts_home.movieData.MoviesModel


interface OnItemClickListener {
    fun onItemClick(movie: MovieData)
}


class MovieInfoAdapter(
    context: Context,
    var movies: List<MovieData>,
    private val itemClickListener: OnItemClickListener
): RecyclerView.Adapter<ViewHolder>() {

    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_ZERO = 0
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var moviesModel = MoviesModel(MoviesDataSourceImpl())

    override fun getItemViewType(position: Int): Int {
        var viewType = VIEW_TYPE_ONE
        if (position == 0) viewType = VIEW_TYPE_ZERO
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //return ViewHolder(inflater.inflate(R.layout.item_movie, parent, false))

        if (viewType == VIEW_TYPE_ONE) {
            return ViewHolder(
                inflater.inflate(R.layout.item_movie, parent, false)
            )
        }
        return ViewHolder(
            inflater.inflate(R.layout.item_movie_soon, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getMovieAt(position)?.let { (holder).bind(it, position, itemClickListener) }
        /*if (position == 0) {
            (holder as ViewHolder2).bind(movies[position], itemClickListener)
        } else {
            (holder as ViewHolder1).bind(movies[position], itemClickListener)
        }*/

    }

    override fun getItemCount(): Int = movies.size


    private fun getMovieAt(position: Int): MovieData? {
        val movies = moviesModel.getMovies()
        return when {
            movies.isEmpty() -> null
            position >= movies.size -> null
            else -> movies[position]
        }
    }

}