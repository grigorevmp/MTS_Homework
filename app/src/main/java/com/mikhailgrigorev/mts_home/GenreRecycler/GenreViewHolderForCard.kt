package com.mikhailgrigorev.mts_home.GenreRecycler

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikhailgrigorev.mts_home.R
import com.mikhailgrigorev.mts_home.api.GenreResponse

class GenreViewHolderForCard(itemView: View): RecyclerView.ViewHolder(itemView) {

    private var parent = itemView.findViewById<TextView>(R.id.item_tag_root)

    fun bind(genre: GenreResponse, clickListener: OnGenreItemClickListener?) {

        parent.text = genre.genre

        if(clickListener != null)
            itemView.setOnClickListener {
                clickListener.onGenreItemClick(genre.genre)
            }
    }
}