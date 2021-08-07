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
        const val VIEW_CARD_MOVIE = 1
        const val VIEW_CARD_HEADER_TITLE = 0
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int): Int {
        var viewType = VIEW_CARD_MOVIE
        if (position == 0) viewType = VIEW_CARD_HEADER_TITLE
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (viewType == VIEW_CARD_MOVIE) {
            return ViewHolder(
                inflater.inflate(R.layout.item_movie, parent, false)
            )
        }
        return ViewHolder(
            inflater.inflate(R.layout.item_header, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position != 0)
            getMovieAt(position - 1)?.let { (holder).bind(it, position, itemClickListener) }
  

    }

    override fun getItemCount(): Int = movies.size + 1


    private fun getMovieAt(position: Int): MovieData? {
        return when {
            movies.isEmpty() -> null
            position >= movies.size -> null
            else -> movies[position]
        }
    }

}