package com.mikhailgrigorev.mts_home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MoviesDetailFragment: Fragment()  {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)

        val safeArgs = MoviesDetailFragmentArgs.fromBundle(requireArguments())

        val bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById<LinearLayout>(R.id.bottomSheet))
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        view.findViewById<TextView>(R.id.movieName)?.apply {
            text = safeArgs.title
        }

        view.findViewById<TextView>(R.id.movie_description)?.apply {
            text = safeArgs.description
        }

        val movieCoverValue = view.findViewById<ImageView>(R.id.movieCover)

        movieCoverValue?.load(safeArgs.imageUrl)

        view.findViewById<TextView>(R.id.ageRating)?.apply {
            text =
                context.getString(R.string.main_age_restriction_text, safeArgs.ageRestriction)
        }

        val ratingbar = view.findViewById<RatingBar>(R.id.ratingbar)

        ratingbar.rating = safeArgs.rateScore.toFloat()

        return view
    }

}