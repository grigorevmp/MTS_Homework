package com.mikhailgrigorev.mts_home.foregroundLoading

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.mikhailgrigorev.mts_home.movieData.Movie
import com.mikhailgrigorev.mts_home.movieData.MoviesModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class ForegroundWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        setForeground(createForegroundInfo())
        return@withContext runCatching {
            loadMovies()
            Result.success()
        }.getOrElse {
            Result.failure()
        }
    }


    private suspend fun loadMovies() {
        val model = MoviesModel()
        model.loadMoviesForeground()
        delay(1000)
    }

    private fun createForegroundInfo(): ForegroundInfo {
        val id = "1616"
        val channelName = "Downloads Notification"
        val title = "Downloading"
        val cancel = "Cancel"
        val body = "Movies list downloading"

        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(getId())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(id, channelName)
        }

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.notification_icon_background)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()

        return ForegroundInfo(1, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(id: String, channelName: String) {
        notificationManager.createNotificationChannel(
            NotificationChannel(id, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        )
    }
}