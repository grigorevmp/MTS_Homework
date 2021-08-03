package com.mikhailgrigorev.mts_home

import android.os.Bundle
import android.os.Parcelable
import android.transition.TransitionManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mikhailgrigorev.mts_home.genreData.GenreModel
import com.mikhailgrigorev.mts_home.movieData.MoviesModel


private const val MOVIES_INITIAL_POSITION = 0

class MainActivity : AppCompatActivity() {

    private var someFragment: MoviesFragment? = null
    private lateinit var moviesModel: MoviesModel
    private lateinit var genreModel: GenreModel
    private lateinit var adapter: MovieInfoAdapter
    private lateinit var adapterGenre: GenreAdapter
    private var state: Parcelable? = null
    private var state2: Parcelable? = null
    private lateinit var recycler: RecyclerView
    private lateinit var recyclerGenre: RecyclerView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = getColor(R.color.cardBackground)

        setContentView(R.layout.activity_main)

        //val fragment: View? = findViewById(R.id.my_nav_host_fragment)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController
        setUpBottomNav(navController)


    }

    private fun setUpBottomNav(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_tab_bar)
        bottomNav?.setupWithNavController(navController)

        bottomNav.setOnItemSelectedListener {item ->
            onNavDestinationSelected(item, Navigation.findNavController(this, R.id.my_nav_host_fragment))
        }
    }

    //val host: NavHostFragment = fragment as NavHostFragment? ?: return
    //navController = host.navController
    //navController.navigate(R.id.navigation_1)
//
//
    //val bottomNavigationBar = findViewById<BottomNavigationView>(R.id.bottom_tab_bar)
    //underlineSelectedItem(-1)
//
    //supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MoviesFragment()).commit()

    //if (savedInstanceState == null) {
    //    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MoviesFragment(), "TAG").commit()
    //} else {
    //    someFragment =
    //        supportFragmentManager.findFragmentByTag("TAG") as? MoviesFragment
    //}



    //}

    /*fun underlineSelectedItem(itemId: Int) {
        val constraintLayout: ConstraintLayout = findViewById(R.id.mainLayout)
        TransitionManager.beginDelayedTransition(constraintLayout)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.setHorizontalBias(
            R.id.underline,
            getItemPosition(itemId) * 1f
        )
        constraintSet.applyTo(constraintLayout)
    }

    private fun getItemPosition(itemId: Int): Int {
        return when (itemId) {
            R.id.home_fragment -> 0
            R.id.profile_fragment -> 1
            else -> 0
        }
    }*/



}