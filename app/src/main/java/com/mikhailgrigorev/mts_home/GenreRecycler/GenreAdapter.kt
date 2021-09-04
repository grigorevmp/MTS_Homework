package com.mikhailgrigorev.mts_home.GenreRecycler

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mikhailgrigorev.mts_home.R
import com.mikhailgrigorev.mts_home.genreData.Genre

interface OnGenreItemClickListener {
    fun onGenreItemClick(genre: String)
}

class GenreAdapter(
    context: Context,
    private val itemClickListener: OnGenreItemClickListener?):
    RecyclerView.Adapter<GenreViewHolder>() {


    private var genres: MutableList<Genre> = ArrayList()

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(inflater.inflate(R.layout.item_tag, parent, false))
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        getGenreAt(position)?.let { holder.bind(it, itemClickListener) }
    }

    override fun getItemCount() = genres.size

    private fun getGenreAt(position: Int): Genre? {
        return when {
            genres.isEmpty() -> null
            position >= genres.size -> null
            else -> genres[position]
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun initData(genres_: List<Genre>?) {
        if (genres_ != null) {
            genres.clear()
            genres.addAll(genres_)
            notifyDataSetChanged()
            Log.d("initDataBlock", "size  = $itemCount")
        }
    }
}