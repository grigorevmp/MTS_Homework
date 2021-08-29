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
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import coil.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.transition.MaterialContainerTransform
import com.mikhailgrigorev.mts_home.ActorsRecycler.ActorAdapter
import com.mikhailgrigorev.mts_home.GenreRecycler.GenreAdapterForCard
import com.mikhailgrigorev.mts_home.api.MovieWithActorsResponse
import com.mikhailgrigorev.mts_home.moviesRecycler.PATH_HEADER
import com.mikhailgrigorev.mts_home.mvvm.MovieCardViewModel
import com.mikhailgrigorev.mts_home.utils.RecyclerViewDecoration

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
    private lateinit var releaseDate: TextView
    private lateinit var adapterGenre: GenreAdapterForCard
    private lateinit var adapterActor: ActorAdapter
    private lateinit var recyclerGenre: RecyclerView
    private lateinit var recyclerActor: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)

        postponeEnterTransition()
        val transition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = ChangeBounds()

        val safeArgs = MoviesDetailFragmentArgs.fromBundle(requireArguments())

        val bottomSheetBehavior =
            BottomSheetBehavior.from(view.findViewById<LinearLayout>(R.id.bottomSheet))
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bottomSheetBehavior.peekHeight = 650
        }

        movieName = view.findViewById(R.id.movieName)
        movieDescription = view.findViewById(R.id.movie_description)
        movieCoverValue = view.findViewById(R.id.movieCover)
        ageRating = view.findViewById(R.id.ageRating)
        ratingbar = view.findViewById(R.id.ratingbar)
        releaseDate = view.findViewById(R.id.release_date)

        movieCoverValue.transitionName = safeArgs.cover
        movieName.transitionName = safeArgs.name

        recyclerGenre = view.findViewById(R.id.genres_container)
        recyclerActor = view.findViewById(R.id.actors_container)

        movieViewModel.currentMovie.observe(viewLifecycleOwner, Observer(::setDataToFragment))
        movieViewModel.viewState.observe(viewLifecycleOwner, Observer(::render))

        movieViewModel.loadMovie(safeArgs.id)

        return view
    }



    private fun setDataToFragment( movie: MovieWithActorsResponse) {
        movieName.apply {
            text = movie.title
        }

        movieDescription.apply {
            text = movie.overview
        }

        releaseDate.apply {
            text = movie.release_date
        }

        adapterGenre = GenreAdapterForCard(this.requireView().context, movie.genres, null)
        adapterGenre.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW

        adapterActor= ActorAdapter(this.requireView().context, movie.credits.cast)
        adapterActor.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW

        recyclerGenre.adapter = adapterGenre
        recyclerActor.adapter = adapterActor

        recyclerGenre.addItemDecoration(RecyclerViewDecoration(0, 6))
        recyclerActor.addItemDecoration(RecyclerViewDecoration(0, 6))

        movieCoverValue.load(PATH_HEADER + movie.poster_path)

        ageRating.apply {
            text =
                context.getString(R.string.main_age_restriction_text, movie.ageRestriction)
        }

        ratingbar.rating = movie.vote_average

        startPostponedEnterTransition()
    }



    private fun render(viewState: ViewState) = with(viewState) {
        if (isDownloading) {
            progressDialog.show()
        } else {
            progressDialog.dismiss()
        }
    }


    data class ViewState(
        val isDownloading: Boolean = false
    )
}