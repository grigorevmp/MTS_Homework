package com.mikhailgrigorev.mts_home.actorData

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ActorRepository {

    private var actorDao: ActorDao
    private var allGenres: List<Actor>

    private val database = ActorDatabase.getInstance()

    init {
        actorDao = database.actorDao()!!
        allGenres = actorDao.getAll()
    }

    fun insert(actor: Actor) {
        CoroutineScope(Dispatchers.IO).launch {
            actorDao.insert(actor)
        }
    }

    fun insertAll(genres: List<Actor>) {
        CoroutineScope(Dispatchers.IO).launch {
            actorDao.insertAll(genres)
        }
    }

    fun update(actor: Actor) {
        CoroutineScope(Dispatchers.IO).launch {
            actorDao.update(actor)
        }
    }

    fun delete(actor: Actor) {
        CoroutineScope(Dispatchers.IO).launch {
            actorDao.delete(actor)
        }
    }

    fun deleteAllActors() {
        CoroutineScope(Dispatchers.IO).launch {
            actorDao.deleteAll()
        }
    }

    fun getAllActors(): List<Actor> {
        return allGenres
    }

    fun getActorById(id: Int): Actor {
        return actorDao.getById(id)!!
    }



}