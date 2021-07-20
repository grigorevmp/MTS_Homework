package com.mikhailgrigorev.mts_home

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mikhailgrigorev.mts_home.movieData.MovieData



class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val movieCover = itemView.findViewById<ImageView>(R.id.list_item_image)
        private val movieName = itemView.findViewById<TextView>(R.id.list_item_movie_name)
        private val movieDesc = itemView.findViewById<TextView>(R.id.list_item_movie_desc)
        private val movieStar1 = itemView.findViewById<ImageView>(R.id.list_item_star1)
        private val movieStar2 = itemView.findViewById<ImageView>(R.id.list_item_star2)
        private val movieStar3 = itemView.findViewById<ImageView>(R.id.list_item_star3)
        private val movieStar4 = itemView.findViewById<ImageView>(R.id.list_item_star4)
        private val movieStar5 = itemView.findViewById<ImageView>(R.id.list_item_star5)
        private val movieAge = itemView.findViewById<TextView>(R.id.list_item_ageRating)

        fun bind(movie: MovieData, position: Int, clickListener: OnItemClickListener) {

            movieCover?.load(movie.imageUrl)
            movieName?.text = movie.title
            movieDesc?.text = movie.description

            when (movie.rateScore) {
                1 ->
                    movieStar1.setBackgroundResource(R.drawable.ic_favorite_full)
                2 -> {
                    movieStar1.setBackgroundResource(R.drawable.ic_favorite_full)
                    movieStar2.setBackgroundResource(R.drawable.ic_favorite_full)
                }
                3 -> {
                    movieStar1.setBackgroundResource(R.drawable.ic_favorite_full)
                    movieStar2.setBackgroundResource(R.drawable.ic_favorite_full)
                    movieStar3.setBackgroundResource(R.drawable.ic_favorite_full)
                }
                4 -> {
                    movieStar1.setBackgroundResource(R.drawable.ic_favorite_full)
                    movieStar2.setBackgroundResource(R.drawable.ic_favorite_full)
                    movieStar3.setBackgroundResource(R.drawable.ic_favorite_full)
                    movieStar4.setBackgroundResource(R.drawable.ic_favorite_full)
                }
                5 -> {
                    movieStar1.setBackgroundResource(R.drawable.ic_favorite_full)
                    movieStar2.setBackgroundResource(R.drawable.ic_favorite_full)
                    movieStar3.setBackgroundResource(R.drawable.ic_favorite_full)
                    movieStar4.setBackgroundResource(R.drawable.ic_favorite_full)
                    movieStar5.setBackgroundResource(R.drawable.ic_favorite_full)
                }
            }

            movieAge?.text =
                itemView.context.getString(R.string.main_age_restriction_text, movie.ageRestriction)

            itemView.setOnClickListener {
                clickListener.onItemClick(movie)
            }
        }


    }
