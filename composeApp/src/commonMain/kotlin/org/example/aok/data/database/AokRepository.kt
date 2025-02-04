package org.example.aok.data.database

class AokRepository(
    private val database: AokDatabase
) {
    val userDao = database.userDao()
//    val postDao = database.postDao()

    fun getAllUsers() = userDao.getAllUsers()
}