package com.mikhailgrigorev.mts_home

import android.os.Bundle
import android.os.Parcelable
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mikhailgrigorev.mts_home.genreData.GenreDataSourceImpl
import com.mikhailgrigorev.mts_home.genreData.GenreModel
import com.mikhailgrigorev.mts_home.movieData.MovieData
import com.mikhailgrigorev.mts_home.movieData.MoviesDataSourceImpl
import com.mikhailgrigorev.mts_home.movieData.MoviesModel
import kotlinx.coroutines.*
import kotlin.random.Random


class MoviesFragment: Fragment(){
    private lateinit var moviesModel: MoviesModel
    private lateinit var genreModel: GenreModel
    private lateinit var adapter: MovieInfoAdapter
    private lateinit var adapterGenre: GenreAdapter
    private var state: Parcelable? = null
    private var state2: Parcelable? = null
    private lateinit var recycler: RecyclerView
    private lateinit var recyclerGenre: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)
        val gd = GridLayoutManager(view.context, 2)


        recycler = view.findViewById(R.id.moviesList)
        val recyclerEmpty = view.findViewById<TextView>(R.id.emptyMoviesList)
        recyclerGenre = view.findViewById(R.id.genreList)

        initDataSource()


        val listener: OnItemClickListener = object : OnItemClickListener {
            override fun onItemClick(movie: MovieData) {
                sendArguments(view,
                    movie.imageUrl,
                    movie.title,
                    movie.description,
                    movie.ageRestriction,
                    movie.rateScore,)

                /*activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, MoviesDetailFragment.
                    newInstance(
                        movie.imageUrl,
                        movie.title,
                        movie.description,
                        movie.ageRestriction,
                        movie.rateScore,
                    )
                    )?.addToBackStack(null)
                ?.commit()*/
            }
        }

        val listenerGenre: OnGenreItemClickListener = object : OnGenreItemClickListener {
            override fun onGenreItemClick(genre: String) {
                showToast(getGenreToastMessage(genre))
            }
        }

        adapter = MovieInfoAdapter(view.context, moviesModel.getMovies(), listener)
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        adapterGenre = GenreAdapter(view.context, genreModel.getGenre(), listenerGenre)
        adapterGenre.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW

        recycler.adapter = adapter
        gd.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.getItemViewType(position) == MovieInfoAdapter.VIEW_CARD_HEADER_TITLE) 2 else 1
            }
        }

        recycler.layoutManager = gd

        recycler.addItemDecoration( RecyclerViewDecoration(20, 50, 2, true))

        recyclerGenre.adapter = adapterGenre

        recycler.addItemDecoration(RecyclerViewDecoration(24, 0))
        recyclerGenre.addItemDecoration(RecyclerViewDecoration(0, 6))

        if (adapter.itemCount == 0) {
            recycler.visibility = View.GONE
            recyclerEmpty.visibility = View.VISIBLE
        }
        val swipeContainer = view.findViewById(R.id.swipeContainer) as SwipeRefreshLayout

        val handler = CoroutineExceptionHandler { _, exception ->
            Log.d("Coroutines error", "handled $exception")
            lifecycleScope.cancel()
            swipeContainer.isRefreshing = false
        }

        swipeContainer.setOnRefreshListener {
            runBlocking {
                val job = lifecycleScope.launch(handler + Job()) {
                    delay(2000)
                    val start = Random.nextInt(0, 3)
                    onMoviesChanged(
                        moviesModel.getMovies()
                            .subList(start, moviesModel.getMovies().size - 3 + start)
                    )
                    swipeContainer.isRefreshing = false
                }
                //job.cancel()
            }
        }


        return view
    }


    private fun initDataSource() {
        moviesModel = MoviesModel(MoviesDataSourceImpl())
        genreModel = GenreModel(GenreDataSourceImpl())
    }


    private fun showToast(message: String?) {
        when {
            message.isNullOrEmpty() -> { showToast(getString(R.string.main_empty_message)) }
            else -> Toast.makeText(view?.context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun sendArguments(view: View, movieImageUrl: String, movieTitle: String,
                      movieDesc: String, movieAge: Int, movieStar: Int) {
        val action = MoviesFragmentDirections.actionOpenMovie(
            movieImageUrl,
            movieTitle,
            movieDesc,
            movieAge,
            movieStar)
        Navigation.findNavController(view).navigate(action)
    }


    private fun getToastMessage(title: Int) =
        getString(R.string.main_click_message, title.toString())

    private fun getGenreToastMessage(title: String) =
        getString(R.string.genre_click_message, title)


    private fun onMoviesChanged (movies: List<MovieData>){
        val callback = MovieCallback(adapter.movies, movies)
        val diff = DiffUtil.calculateDiff(callback)
        adapter.movies = movies
        diff.dispatchUpdatesTo(adapter)

    }



}