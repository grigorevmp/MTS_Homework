package com.mikhailgrigorev.mts_home

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mikhailgrigorev.mts_home.foregroundLoading.ForegroundWorker
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = getColor(R.color.cardBackground)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContentView(R.layout.activity_main)

        scheduleWork(this)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController
        setUpBottomNav(navController)
    }

    private fun scheduleWork(context: Context) {
        val constraintsBuilder: Constraints.Builder = Constraints.Builder()
        constraintsBuilder.setRequiresBatteryNotLow(false)
        constraintsBuilder.setRequiredNetworkType(NetworkType.CONNECTED)
        constraintsBuilder.setRequiresCharging(false)
        constraintsBuilder.setRequiresDeviceIdle(false)

        val constraints = constraintsBuilder.build()

        val moviesCheckBuilder = PeriodicWorkRequest.Builder(
            ForegroundWorker::class.java, 1, TimeUnit.DAYS
        )

        moviesCheckBuilder.setConstraints(constraints)

        val request = moviesCheckBuilder.build()
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork("DownloadingMovies", ExistingPeriodicWorkPolicy.KEEP, request)
    }

    private fun setUpBottomNav(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_tab_bar)
        bottomNav?.setupWithNavController(navController)

        bottomNav.setOnItemSelectedListener {item ->
            onNavDestinationSelected(item, Navigation.findNavController(this, R.id.my_nav_host_fragment))
        }
    }


}