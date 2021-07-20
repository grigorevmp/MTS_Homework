package com.mikhailgrigorev.mts_home

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.transition.TransitionManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mikhailgrigorev.mts_home.genreData.GenreModel
import com.mikhailgrigorev.mts_home.movieData.*


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = getColor(R.color.cardBackground)

        setContentView(R.layout.activity_main)

        val bottomNavigationBar = findViewById<BottomNavigationView>(R.id.bottom_tab_bar)
        underlineSelectedItem(-1)

        //supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MoviesFragment()).commit()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MoviesFragment(), "TAG").commit()
        } else {
            someFragment =
                supportFragmentManager.findFragmentByTag("TAG") as? MoviesFragment
        }


        bottomNavigationBar.setOnItemSelectedListener {


            underlineSelectedItem(it.itemId)

            val selectedFragment: Fragment?
            when (it.itemId) {
                R.id.homeFragment -> {
                    selectedFragment = MoviesFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
                    return@setOnItemSelectedListener true
                }
                R.id.profileFragment -> {
                    selectedFragment = ProfileFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }
    }

    private fun underlineSelectedItem(itemId: Int) {
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
            R.id.homeFragment -> 0
            R.id.profileFragment -> 1
            else -> 0
        }
    }


    override fun onPause() {
        super.onPause()
        //state = recycler.layoutManager!!.onSaveInstanceState()
        //state2 = recyclerGenre.layoutManager!!.onSaveInstanceState()
    }

    override fun onResume() {
       super.onResume()
       //if (state != null)
       //    recycler.layoutManager!!.onRestoreInstanceState(state)
       //if (state2 != null)
       //    recyclerGenre.layoutManager!!.onRestoreInstanceState(state2)


    }



}