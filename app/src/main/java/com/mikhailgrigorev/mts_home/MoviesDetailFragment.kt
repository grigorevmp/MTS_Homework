package com.mikhailgrigorev.mts_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mikhailgrigorev.mts_home.movieData.MovieData

class MoviesDetailFragment: Fragment()  {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)

        val bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById<LinearLayout>(R.id.bottomSheet))
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        val movieNameValue = view.findViewById<TextView>(R.id.movieName)?.apply {
            text = arguments?.getString("MovieTitle")
        }

        val movieDescriptionValue = view.findViewById<TextView>(R.id.movie_description)?.apply {
            text = arguments?.getString("MovieDesc")
        }

        val movieCoverValue = view.findViewById<ImageView>(R.id.movieCover)

        movieCoverValue?.load(arguments?.getString("MovieImageUrl"))

        val ageRatingValue = view.findViewById<TextView>(R.id.ageRating)?.apply {
            text =
                context.getString(R.string.main_age_restriction_text, arguments?.getInt("MovieAge"))
        }

        val movieStar1 = view.findViewById<ImageView>(R.id.star1)
        val movieStar2 = view.findViewById<ImageView>(R.id.star2)
        val movieStar3 = view.findViewById<ImageView>(R.id.star3)
        val movieStar4 = view.findViewById<ImageView>(R.id.star4)
        val movieStar5 = view.findViewById<ImageView>(R.id.star5)

        when (arguments?.getInt("MovieRate")) {
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



        return view
    }

    companion object {
        fun newInstance(movieImageUrl: String, movieTitle: String,
                        movieDesc: String, movieAge: Int, movieStar: Int): MoviesDetailFragment {
            val args = Bundle()
            args.putString("MovieImageUrl", movieImageUrl)
            args.putString("MovieTitle", movieTitle)
            args.putString("MovieDesc", movieDesc)
            args.putInt("MovieAge", movieAge)
            args.putInt("MovieRate", movieStar)
            val fragment = MoviesDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }


}