package com.mikhailgrigorev.mts_home.userData

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserRepository {

    private var userDao: UserDao

    private val database = UserDatabase.getInstance()

    init {
        userDao = database.userDao()!!
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

    fun deleteAllUsers() {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.deleteAll()
        }
    }

    suspend fun getUserById(id: Long): User {
        return userDao.getById(id)!!
    }

    fun getUserByLogin(login: String): User? {
        return userDao.getByLogin(login)
    }


}