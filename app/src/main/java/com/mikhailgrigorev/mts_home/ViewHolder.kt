package com.mikhailgrigorev.mts_home

import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mikhailgrigorev.mts_home.api.MovieResponse
import com.mikhailgrigorev.mts_home.movieData.OnItemClickListener

const val PATH_HEADER = "https://www.themoviedb.org/t/p/w600_and_h900_bestv2"

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val movieCover = itemView.findViewById<ImageView>(R.id.list_item_image)
        private val movieName = itemView.findViewById<TextView>(R.id.list_item_movie_name)
        private val movieDesc = itemView.findViewById<TextView>(R.id.list_item_movie_desc)
        private val ratingbar = itemView.findViewById<RatingBar>(R.id.ratingbar)
        private val movieAge = itemView.findViewById<TextView>(R.id.list_item_ageRating)

        fun bind(movie: MovieResponse, position: Int, clickListener: OnItemClickListener) {

            val uri = PATH_HEADER + movie.poster_path

            movieCover?.load(uri)
            movieName?.text = movie.title
            movieDesc?.text = movie.overview

            ratingbar.rating = movie.vote_average

            if(movie.adult)
                movieAge?.text =
                    itemView.context.getString(R.string.main_age_restriction_text, 18)
            else
                movieAge?.visibility = View.GONE

            itemView.setOnClickListener {
                clickListener.onItemClick(movie)
            }
        }


    }
