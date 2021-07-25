package com.mikhailgrigorev.mts_home

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikhailgrigorev.mts_home.genreData.GenreDataSourceImpl
import com.mikhailgrigorev.mts_home.genreData.GenreModel
import com.mikhailgrigorev.mts_home.movieData.MovieData
import com.mikhailgrigorev.mts_home.movieData.MoviesDataSourceImpl
import com.mikhailgrigorev.mts_home.movieData.MoviesModel


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
        //return super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)
        val gd = GridLayoutManager(view.context, 2)

        recycler = view.findViewById(R.id.moviesList)
        val recyclerEmpty = view.findViewById<TextView>(R.id.emptyMoviesList)
        recyclerGenre = view.findViewById(R.id.genreList)

        initDataSource()


        val listener: OnItemClickListener = object : OnItemClickListener {
            override fun onItemClick(movie: MovieData) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, MoviesDetailFragment.
                    newInstance(
                        movie.imageUrl,
                        movie.title,
                        movie.description,
                        movie.ageRestriction,
                        movie.rateScore,
                    )
                    )?.addToBackStack(null)
                ?.commit()
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


    private fun getToastMessage(title: Int) =
        getString(R.string.main_click_message, title.toString())

    private fun getGenreToastMessage(title: String) =
        getString(R.string.genre_click_message, title)


    fun onMoviesChanged (movies: List<MovieData>){
        val callback = MovieCallback(adapter.movies, movies)
        val diff = DiffUtil.calculateDiff(callback)
        adapter.movies = movies
        diff.dispatchUpdatesTo(adapter)

    }

   // val BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    //val BUNDLE_RECYCLER_LAYOUT2 = "classname.recycler.layout";

    /*private fun restorePreviousState(savedInstanceState: Bundle) {
        val savedRecyclerLayoutState =
            savedInstanceState.getParcelable<Parcelable>(BUNDLE_RECYCLER_LAYOUT)
        //val savedRecyclerLayoutState2 =
        //savedInstanceState.getParcelable<Parcelable>(BUNDLE_RECYCLER_LAYOUT2)
        recycler.layoutManager!!.onRestoreInstanceState(savedRecyclerLayoutState)


        if (state != null)
            recycler.layoutManager!!.onRestoreInstanceState(state)

        //recyclerGenre.layoutManager!!.onRestoreInstanceState(savedRecyclerLayoutState2)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            val savedRecyclerLayoutState =
                savedInstanceState.getParcelable<Parcelable>(BUNDLE_RECYCLER_LAYOUT)
            //val savedRecyclerLayoutState2 =
                //savedInstanceState.getParcelable<Parcelable>(BUNDLE_RECYCLER_LAYOUT2)
            recycler.layoutManager!!.onRestoreInstanceState(savedRecyclerLayoutState)
            Toast.makeText(this.context, "ft", Toast.LENGTH_SHORT).show()
            //recyclerGenre.layoutManager!!.onRestoreInstanceState(savedRecyclerLayoutState2)
        }
    }

   /* override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
            outState.putParcelable(
                BUNDLE_RECYCLER_LAYOUT,
                recycler.layoutManager!!.onSaveInstanceState()

            )
        state = recycler.layoutManager!!.onSaveInstanceState()

    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        state = recycler.layoutManager!!.onSaveInstanceState()
        state2 = recyclerGenre.layoutManager!!.onSaveInstanceState()
    }

   /* override fun onAttach(context: Context) {
        super.onAttach(context)
        if (state != null)
            recycler.layoutManager!!.onRestoreInstanceState(state)
        if (state2 != null)
            recyclerGenre.layoutManager!!.onRestoreInstanceState(state2)


    }*/*/

}