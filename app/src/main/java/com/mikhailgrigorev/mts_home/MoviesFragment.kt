package com.mikhailgrigorev.mts_home

import android.app.ProgressDialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mikhailgrigorev.mts_home.GenreRecycler.GenreAdapter
import com.mikhailgrigorev.mts_home.GenreRecycler.OnGenreItemClickListener
import com.mikhailgrigorev.mts_home.api.MovieResponse
import com.mikhailgrigorev.mts_home.movieData.Movie
import com.mikhailgrigorev.mts_home.moviesRecycler.MoviesAdapter
import com.mikhailgrigorev.mts_home.moviesRecycler.OnItemClickListener
import com.mikhailgrigorev.mts_home.mvvm.GenresViewModel
import com.mikhailgrigorev.mts_home.mvvm.MoviesViewModel
import com.mikhailgrigorev.mts_home.network.NetworkManager
import com.mikhailgrigorev.mts_home.network.NetworkManagerImpl
import com.mikhailgrigorev.mts_home.utils.RecyclerViewDecoration
import kotlinx.coroutines.*
import android.view.animation.LayoutAnimationController
import android.view.animation.OvershootInterpolator
import android.widget.*
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.doOnPreDraw
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator


class MoviesFragment : Fragment(), NetworkManager.OnNetworkStateChangeListener {
    private lateinit var adapter: MoviesAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var recyclerEmpty: TextView
    private lateinit var adapterGenre: GenreAdapter
    private lateinit var recyclerGenre: RecyclerView
    private lateinit var progressBar: ProgressBar


    private val movieViewModel: MoviesViewModel by viewModels()
    private val genreViewModel: GenresViewModel by viewModels()

    private val progressDialog by lazy { ProgressDialog.show(this.context, "", getString(R.string.please_wait)) }

    private var hasData: Boolean = false
    private var networkStateManager: NetworkManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)

        postponeEnterTransition()

        var gd = GridLayoutManager(view.context, 2)
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gd = GridLayoutManager(view.context, 4)
        }

        init()

        progressBar = view.findViewById(R.id.progress_bar)


        recycler = view.findViewById(R.id.moviesList)

        recyclerEmpty = view.findViewById(R.id.emptyMoviesList)
        recyclerGenre = view.findViewById(R.id.genreList)

        recycler.itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))
        recyclerGenre.itemAnimator = SlideInUpAnimator(OvershootInterpolator(1f))

        val resId: Int = R.anim.grid_layout_animation_from_bottom
        val animation: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(context, resId)
        recycler.layoutAnimation = animation

        val listener: OnItemClickListener = object : OnItemClickListener {
            override fun onItemClick(
                movie: Movie,
                movieCover: ImageView,
                movieName: TextView,
                parent_item: LinearLayout,
                ratingbar: RatingBar,
                position: Int
            ) {
                sendArguments(
                    view,
                    movie.id,
                    movieCover,
                    movieName,
                    parent_item,
                    ratingbar,
                    position
                )
            }
        }

        val listenerGenre: OnGenreItemClickListener = object : OnGenreItemClickListener {
            override fun onGenreItemClick(genre: String) {
                showToast(getGenreToastMessage(genre))
            }
        }

        adapter = MoviesAdapter(view.context, listener)
        adapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW
        adapterGenre = GenreAdapter(view.context, listenerGenre)
        adapterGenre.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW


        recycler.adapter = adapter
        recycler.layoutManager = gd
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recycler.addItemDecoration(RecyclerViewDecoration(20, 50, 4, true))
        } else {
            recycler.addItemDecoration(RecyclerViewDecoration(20, 50, 2, true))
        }

        networkStateManager = context?.let { NetworkManagerImpl(it) }
        networkStateManager?.register(this)

        movieViewModel.dataList.observe(viewLifecycleOwner, Observer(adapter::initData))
        movieViewModel.viewState.observe(viewLifecycleOwner, Observer(::render))

        genreViewModel.dataList.observe(viewLifecycleOwner, Observer(adapterGenre::initData))
        genreViewModel.viewState.observe(viewLifecycleOwner, Observer(::render))

        movieViewModel.loadMovies()
        genreViewModel.loadGenres()

        gd.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (adapter.getItemViewType(position) == MoviesAdapter.VIEW_CARD_HEADER_TITLE) 4 else 1
                } else {
                    if (adapter.getItemViewType(position) == MoviesAdapter.VIEW_CARD_HEADER_TITLE) 2 else 1
                }
            }
        }

        recycler.adapter = adapter

        recyclerGenre.adapter = adapterGenre

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
                lifecycleScope.launch(handler + Job()) {
                    movieViewModel.loadMovies()
                    swipeContainer.isRefreshing = false
                }
            }
        }

        view.findViewById<MotionLayout>(R.id.motionLayout2).transitionToStart()


        recycler.post { startPostponedEnterTransition() }
        recyclerGenre.post { startPostponedEnterTransition() }
        view.doOnPreDraw { startPostponedEnterTransition() }

        return view
    }

    private fun init(){
        networkStateManager = context?.let { NetworkManagerImpl(it) }
        networkStateManager?.register(this)

    }


    private fun requestData() {
        if (hasData) return

        viewLifecycleOwner.lifecycleScope.launch {
            showProgressBar(isShow = true)

            try {
                movieViewModel.dataList.observe(viewLifecycleOwner, Observer(adapter::initData))
                movieViewModel.viewState.observe(viewLifecycleOwner, Observer(::render))
                movieViewModel.loadMovies()
                processRequestResult(isSuccess = true)
            } catch (e: Exception) {
                processRequestResult(isSuccess = false)
            }
        }
    }


    private fun showToast(message: String?) {
        when {
            message.isNullOrEmpty() -> { showToast(getString(R.string.main_empty_message)) }
            else -> Toast.makeText(view?.context, message, Toast.LENGTH_SHORT).show()
        }
    }


    fun sendArguments(
        view: View,
        movieId: Int,
        movieCover: ImageView,
        movieName: TextView,
        parent_item: LinearLayout,
        ratingBar: RatingBar,
        position: Int
    ) {
        val action = MoviesFragmentDirections.actionOpenMovie(
            movieId,
            position
        )
        val extras = FragmentNavigatorExtras(
            movieCover to movieCover.transitionName,
            movieName to movieName.transitionName,
            parent_item to parent_item.transitionName,
            ratingBar to ratingBar.transitionName,
        )
        Navigation.findNavController(view).navigate(action, extras)
            movieId.toLong()
        )
        Navigation.findNavController(view).navigate(action)
    }

    private fun getGenreToastMessage(title: String) =
        getString(R.string.genre_click_message, title)


    private fun onMoviesChanged(movies: List<MovieResponse>) {
        //val callback = MovieCallback(adapter.movies, movies)
        //val diff = DiffUtil.calculateDiff(callback)
        //adapter.movies = movies as MutableList<MovieData>
        //diff.dispatchUpdatesTo(adapter)
    }

    data class ViewState(
        val isDownloading: Boolean = false
    )

    private fun render(viewState: ViewState) = with(viewState) {
        if (isDownloading) {
            progressDialog.show()
        } else {
            progressDialog.dismiss()
        }
    }

    private fun showProgressBar(isShow: Boolean) {
        activity?.runOnUiThread {
            if (isShow) progressBar.visibility=View.VISIBLE else progressBar.visibility=View.GONE
        }
    }

    private fun processRequestResult(isSuccess: Boolean) {
        showProgressBar(isShow = false)

        if (isSuccess) {
            hasData = true
            recycler.visibility=View.VISIBLE
        } else {
            recycler.visibility=View.GONE
        }
    }

    override fun onNetworkStateChanged(isConnected: Boolean) {
        if (isConnected) requestData()
    }


}