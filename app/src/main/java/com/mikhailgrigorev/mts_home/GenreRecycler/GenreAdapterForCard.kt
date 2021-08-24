package com.mikhailgrigorev.mts_home.GenreRecycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mikhailgrigorev.mts_home.R
import com.mikhailgrigorev.mts_home.api.GenreResponse

class GenreAdapterForCard(
    context: Context,
    private var genres: List<GenreResponse>,
    private val itemClickListener: OnGenreItemClickListener?):
    RecyclerView.Adapter<GenreViewHolderForCard>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolderForCard {
        return GenreViewHolderForCard(inflater.inflate(R.layout.item_tag, parent, false))
    }

    override fun onBindViewHolder(holder: GenreViewHolderForCard, position: Int) {
        getGenreAt(position)?.let { holder.bind(it, itemClickListener) }
    }

    override fun getItemCount() = genres.size

    private fun getGenreAt(position: Int): GenreResponse? {
        return when {
            genres.isEmpty() -> null
            position >= genres.size -> null
            else -> genres[position]
        }
    }
}