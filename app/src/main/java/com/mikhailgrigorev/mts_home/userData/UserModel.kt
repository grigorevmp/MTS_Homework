package com.mikhailgrigorev.mts_home.userData

import android.app.Application
import android.content.ContentValues
import android.os.AsyncTask
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlin.random.Random

fun <P, R> CoroutineScope.executeAsyncTask(
    doInBackground: suspend (suspend (P) -> Unit) -> R,
    onPostExecute: (R) -> Unit
) = launch {
    val result = withContext(Dispatchers.IO) {
        doInBackground {
            withContext(Dispatchers.Main) {}
        }
    }
    onPostExecute(result)
}

class UserModel : UserModelApi {

    override fun loadUser(callback: LoadUserByIdCallback?, id: Long) {
        val loadUserTask = LoadUserTask(callback, id)
        loadUserTask.execute()
    }

    override fun addUser(contentValues: ContentValues?, callback: CompleteCallback?) {
        val addUserTask = AddUserTask(callback)
        addUserTask.execute(contentValues)
    }

    override fun clearUsers(completeCallback: CompleteCallback?) {
        val clearUsersTask = ClearUsersTask(completeCallback)
        clearUsersTask.execute()
    }

    interface LoadUserCallback {
        fun onLoad(movies: List<User>?)
    }

    interface LoadUserByIdCallback {
        fun onLoad(user: User?)
    }

    interface CompleteCallback {
        fun onComplete()
    }
}

class LoadUserTask(
    private val callback: UserModel.LoadUserByIdCallback?,
    private val id: Long
) : ViewModel() {
    fun execute() = viewModelScope.launch {
        onPreExecute()
        val result = doInBackground() // runs in background thread without blocking the Main Thread
        onPostExecute(result)
    }

    private suspend fun doInBackground(): User = withContext(Dispatchers.IO) { // to run code in Background Thread
        // do async work
        val userRepo = UserRepository()
        return@withContext userRepo.getUserById(id)
    }

    // Runs on the Main(UI) Thread
    private fun onPreExecute() {
        // show progress
    }

    // Runs on the Main(UI) Thread
    private fun onPostExecute(user: User) {
        callback?.onLoad(user)
    }
}

    // AsyncTask<Void?, Void?, User>() {
    // override fun doInBackground(vararg params: Void?): User {
    //     val userRepo = UserRepository()
    //     return userRepo.getUserById(id)
    // }
//
    // override fun onPostExecute(user: User) {
    //     callback?.onLoad(user)
    // }


class AddUserTask(
    private val callback: UserModel.CompleteCallback?
) :
    AsyncTask<ContentValues?, Void?, Void?>() {
    override fun doInBackground(vararg params: ContentValues?): Void? {
        return null
    }

    override fun onPostExecute(aVoid: Void?) {
        super.onPostExecute(aVoid)
        callback?.onComplete()
    }
}

class ClearUsersTask(
    private val callback: UserModel.CompleteCallback?
) :
    AsyncTask<Void?, Void?, Void?>() {
    override fun doInBackground(vararg params: Void?): Void? {
        return null
    }

    override fun onPostExecute(aVoid: Void?) {
        super.onPostExecute(aVoid)
        callback?.onComplete()
    }
}

interface UserModelApi {
    fun loadUser(callback: UserModel.LoadUserByIdCallback?, id: Long)
    fun addUser(contentValues: ContentValues?, callback: UserModel.CompleteCallback?)
    fun clearUsers(completeCallback: UserModel.CompleteCallback?)
}