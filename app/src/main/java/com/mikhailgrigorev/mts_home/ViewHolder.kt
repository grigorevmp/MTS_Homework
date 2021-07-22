package com.mikhailgrigorev.mts_home

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mikhailgrigorev.mts_home.movieData.MovieData



class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val movieCover = itemView.findViewById<ImageView>(R.id.list_item_image)
        private val movieName = itemView.findViewById<TextView>(R.id.list_item_movie_name)
        private val movieDesc = itemView.findViewById<TextView>(R.id.list_item_movie_desc)
        private val ratingbar = itemView.findViewById<RatingBar>(R.id.ratingbar)
        private val movieAge = itemView.findViewById<TextView>(R.id.list_item_ageRating)

        fun bind(movie: MovieData, position: Int, clickListener: OnItemClickListener) {

            movieCover?.load(movie.imageUrl)
            movieName?.text = movie.title
            movieDesc?.text = movie.description

            ratingbar.rating = movie.rateScore.toFloat()

            movieAge?.text =
                itemView.context.getString(R.string.main_age_restriction_text, movie.ageRestriction)

            itemView.setOnClickListener {
                clickListener.onItemClick(movie)
            }
        }


    }
