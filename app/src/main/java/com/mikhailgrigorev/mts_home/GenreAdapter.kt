package com.mikhailgrigorev.mts_home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mikhailgrigorev.mts_home.genreData.GenreData
import com.mikhailgrigorev.mts_home.genreData.GenreDataSourceImpl
import com.mikhailgrigorev.mts_home.genreData.GenreModel
import com.mikhailgrigorev.mts_home.movieData.MovieData
import com.mikhailgrigorev.mts_home.movieData.MoviesDataSourceImpl
import com.mikhailgrigorev.mts_home.movieData.MoviesModel

interface OnGenreItemClickListener {
    fun onGenreItemClick(genre: String)
}

class GenreAdapter(
    context: Context,
    var genres: List<GenreData>,
    private val itemClickListener: OnGenreItemClickListener):
    RecyclerView.Adapter<GenreViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private var genreModel = GenreModel(GenreDataSourceImpl())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(inflater.inflate(R.layout.item_tag, parent, false))
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        getGenreAt(position)?.let { holder.bind(it, itemClickListener) }
    }

    override fun getItemCount() = genres.size

    private fun getGenreAt(position: Int): GenreData? {
        val genres = genreModel.getGenre()
        return when {
            genres.isEmpty() -> null
            position >= genres.size -> null
            else -> genres[position]
        }
    }
}