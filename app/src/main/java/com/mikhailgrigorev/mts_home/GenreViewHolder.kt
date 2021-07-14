package com.mikhailgrigorev.mts_home

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikhailgrigorev.mts_home.genreData.GenreData

class GenreViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private var parent = itemView.findViewById<TextView>(R.id.item_tag_root)

    fun bind(genre: GenreData, clickListener: OnGenreItemClickListener) {

        parent.text = genre.genre

        itemView.setOnClickListener {
            clickListener.onGenreItemClick(genre.genre)
        }
    }


}