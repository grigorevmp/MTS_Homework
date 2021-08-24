package com.mikhailgrigorev.mts_home.GenreRecycler

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mikhailgrigorev.mts_home.R

class GenreViewHolderForCard(itemView: View): RecyclerView.ViewHolder(itemView) {

    private var parent = itemView.findViewById<TextView>(R.id.item_tag_root)

    fun bind(genre: String, clickListener: OnGenreItemClickListener?) {

        parent.text = genre

        if(clickListener != null)
            itemView.setOnClickListener {
                clickListener.onGenreItemClick(genre)
            }
    }
}