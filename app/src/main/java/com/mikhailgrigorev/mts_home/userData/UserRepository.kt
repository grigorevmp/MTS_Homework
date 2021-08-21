package com.mikhailgrigorev.mts_home.userData

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepository {

    private var userDao: UserDao
    private var allUsers: List<User>

    private val database = UserDatabase.getInstance()

    init {
        userDao = database.userDao()!!
        allUsers = userDao.getAll()
    }

    fun insert(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.insert(user)
        }
    }

    fun update(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.update(user)
        }
    }

    fun delete(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.delete(user)
        }
    }

    fun deleteAllMovies() {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.deleteAll()
        }
    }

    fun getAllUsers(): List<User> {
        return allUsers
    }

    fun getUserById(id: Long): User {
        return userDao.getById(id)!!
    }

    fun getUserByLogin(login: String): User? {
        return userDao.getByLogin(login)
    }


}