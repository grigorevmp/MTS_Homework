package com.mikhailgrigorev.mts_home

import android.app.ProgressDialog
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mikhailgrigorev.mts_home.api.MovieResponse
import com.mikhailgrigorev.mts_home.mvvm.MovieCardViewModel

class MoviesDetailFragment: Fragment() {

    private val movieViewModel: MovieCardViewModel by viewModels()

    private val progressDialog by lazy {
        ProgressDialog.show(
            this.context,
            "",
            getString(R.string.please_wait)
        )
    }

    private lateinit var movieName: TextView
    private lateinit var movieDescription: TextView
    private lateinit var movieCoverValue: ImageView
    private lateinit var ageRating: TextView
    private lateinit var ratingbar: RatingBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)

        val safeArgs = MoviesDetailFragmentArgs.fromBundle(requireArguments())

        val bottomSheetBehavior =
            BottomSheetBehavior.from(view.findViewById<LinearLayout>(R.id.bottomSheet))
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bottomSheetBehavior.peekHeight=600
        }

        movieName = view.findViewById(R.id.movieName)
        movieDescription = view.findViewById(R.id.movie_description)
        movieCoverValue = view.findViewById(R.id.movieCover)
        ageRating = view.findViewById(R.id.ageRating)
        ratingbar = view.findViewById(R.id.ratingbar)

        movieViewModel.currentMovie.observe(viewLifecycleOwner, Observer(::setDataToFragment))
        movieViewModel.viewState.observe(viewLifecycleOwner, Observer(::render))

        movieViewModel.loadMovie(safeArgs.id)

        return view
    }

    private fun setDataToFragment(movie: MovieResponse) {
        movieName.apply {
            text = movie.title
        }

        movieDescription.apply {
            text = movie.overview
        }


        movieCoverValue.load(movie.poster_path)

        ageRating.apply {
            text =
                context.getString(R.string.main_age_restriction_text, movie.ageRestriction)
        }

        ratingbar.rating = movie.vote_average.toFloat()
    }

    private fun render(viewState: ViewState) = with(viewState) {
        if (isDownloaded) {
            progressDialog.show()
        } else {
            progressDialog.dismiss()
        }
    }


    data class ViewState(
        val isDownloaded: Boolean = false
    )
}