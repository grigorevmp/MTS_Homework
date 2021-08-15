package com.mikhailgrigorev.mts_home

import android.app.ProgressDialog
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.mikhailgrigorev.mts_home.MovieResponse.BaseMoviesModel
import com.mikhailgrigorev.mts_home.api.MovieResponse
import com.mikhailgrigorev.mts_home.api.ObjectResponse
import com.mikhailgrigorev.mts_home.genreData.GenreDataSourceImpl
import com.mikhailgrigorev.mts_home.genreData.GenreModel
import com.mikhailgrigorev.mts_home.movieData.*
import com.mikhailgrigorev.mts_home.mvvm.MoviesViewModel
import com.mikhailgrigorev.mts_home.network.NetworkManager
import com.mikhailgrigorev.mts_home.network.NetworkManagerImpl
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MoviesFragment : Fragment(), NetworkManager.OnNetworkStateChangeListener {
    private lateinit var baseMoviesModel: BaseMoviesModel
    private lateinit var genreModel: GenreModel
    private lateinit var adapter: MoviesAdapter
    private lateinit var adapterGenre: GenreAdapter
    private lateinit var recycler: RecyclerView
    private lateinit var recyclerEmpty: TextView
    private lateinit var recyclerGenre: RecyclerView
    private lateinit var progressBar: ProgressBar


    private val movieViewModel: MoviesViewModel by viewModels()

    private val progressDialog by lazy { ProgressDialog.show(this.context, "", getString(R.string.please_wait)) }

    private var hasData: Boolean = false
    private var networkStateManager: NetworkManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)

        var gd = GridLayoutManager(view.context, 2)
        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gd = GridLayoutManager(view.context, 4)
        }

        progressBar = view.findViewById(R.id.progress_bar)

        recycler = view.findViewById(R.id.moviesList)
        recyclerEmpty = view.findViewById(R.id.emptyMoviesList)
        recyclerGenre = view.findViewById(R.id.genreList)

        initDataSource()


        val listener: OnItemClickListener = object : OnItemClickListener {
            override fun onItemClick(movie: MovieResponse) {
                sendArguments(
                    view,
                    movie.id
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
        adapterGenre = GenreAdapter(view.context, genreModel.getGenre(), listenerGenre)
        adapterGenre.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.ALLOW


        recycler.adapter = adapter
        recycler.layoutManager = gd
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recycler.addItemDecoration(RecyclerViewDecoration(20, 50, 4, true))
        } else {
            recycler.addItemDecoration(RecyclerViewDecoration(20, 50, 2, true))
        }

        // movieViewModel.dataList.observe(viewLifecycleOwner, Observer(adapter::initData))
        // movieViewModel.viewState.observe(viewLifecycleOwner, Observer(::render))

        // movieViewModel.loadMovies()

        networkStateManager = context?.let { NetworkManagerImpl(it) }
        networkStateManager?.register(this)
        var result: List<MovieResponse> = arrayListOf()
        showProgressBar(isShow = true)
        App.instance.apiService.getMovies().enqueue(object : Callback<ObjectResponse> {
            override fun onResponse(
                call: Call<ObjectResponse>,
                response: Response<ObjectResponse>
            ) {
                result = response.body()?.results ?: emptyList()
                adapter.initData(result)
                recycler.adapter = adapter
                processRequestResult(isSuccess = true)
            }

            override fun onFailure(call: Call<ObjectResponse>, t: Throwable) {
                t.printStackTrace()
                processRequestResult(isSuccess = false)
            }
        })


        gd.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (adapter.getItemViewType(position) == MoviesAdapter.VIEW_CARD_HEADER_TITLE) 4 else 1
                } else {
                    if (adapter.getItemViewType(position) == MoviesAdapter.VIEW_CARD_HEADER_TITLE) 2 else 1
                }
            }
        }

        adapter.initData(result)
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
                val job = lifecycleScope.launch(handler + Job()) {
                    //onMoviesChanged(
                    //    baseMoviesModel.getRandomMovies()
                    //)
                    swipeContainer.isRefreshing = false
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // init()
        //requestData()
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

    private fun initDataSource() {
        baseMoviesModel = BaseMoviesModel(MoviesDataSourceImpl())
        genreModel = GenreModel(GenreDataSourceImpl())
    }


    private fun showToast(message: String?) {
        when {
            message.isNullOrEmpty() -> { showToast(getString(R.string.main_empty_message)) }
            else -> Toast.makeText(view?.context, message, Toast.LENGTH_SHORT).show()
        }
    }


    fun sendArguments(view: View, movieId: Int) {
        val action = MoviesFragmentDirections.actionOpenMovie(
            movieId)
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
        val isDownloaded: Boolean = false
    )

    private fun render(viewState: ViewState) = with(viewState) {
        if (isDownloaded) {
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