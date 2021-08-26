package com.mikhailgrigorev.mts_home.userData

import android.content.ContentValues
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

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
        addUserTask.execute()
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
        val result = doInBackground()
        onPostExecute(result)
    }

    private suspend fun doInBackground(): User = withContext(Dispatchers.IO) {
        val userRepo = UserRepository()
        return@withContext userRepo.getUserById(id)
    }

    private fun onPostExecute(user: User) {
        callback?.onLoad(user)
    }
}

class AddUserTask(
    private val callback: UserModel.CompleteCallback?
) : ViewModel() {
    fun execute() = viewModelScope.launch {
        doInBackground()
        onPostExecute()
    }

    private suspend fun doInBackground(): User? = withContext(Dispatchers.IO) {
        return@withContext null
    }

    private fun onPostExecute() {
        callback?.onComplete()
    }
}

class ClearUsersTask(
    private val callback: UserModel.CompleteCallback?
) : ViewModel() {
    fun execute() = viewModelScope.launch {
        doInBackground()
        onPostExecute()
    }

    private suspend fun doInBackground(): User? = withContext(Dispatchers.IO) {
        return@withContext null
    }

    private fun onPostExecute() {
        callback?.onComplete()
    }
}


interface UserModelApi {
    fun loadUser(callback: UserModel.LoadUserByIdCallback?, id: Long)
    fun addUser(contentValues: ContentValues?, callback: UserModel.CompleteCallback?)
    fun clearUsers(completeCallback: UserModel.CompleteCallback?)
}