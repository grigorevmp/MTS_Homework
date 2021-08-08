package com.mikhailgrigorev.mts_home.movieData

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mikhailgrigorev.mts_home.R
import com.mikhailgrigorev.mts_home.ViewHolder


interface OnItemClickListener {
    fun onItemClick(movie: Movie)
}


class MoviesAdapter(
    context: Context,
    private val itemClickListener: OnItemClickListener

) : RecyclerView.Adapter<ViewHolder>() {
    var movies: MutableList<Movie> = ArrayList()

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
        if (position != 0)
            getMovieAt(position - 1)?.let { (holder).bind(it, position, itemClickListener) }


    }

    fun initData(movies_: List<Movie>?) {
        if (movies_ != null) {
            movies.clear()
            movies.addAll(movies_)
            notifyDataSetChanged()
            Log.d("initDataBlock", "size  = $itemCount")
        }
    }

    override fun getItemCount(): Int = movies.size + 1


    private fun getMovieAt(position: Int): Movie? {
        return when {
            movies.isEmpty() -> null
            position >= movies.size -> null
            else -> movies[position]
        }
    }

}