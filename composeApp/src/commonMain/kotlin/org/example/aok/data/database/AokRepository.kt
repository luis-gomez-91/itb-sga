package org.example.aok.data.database

class AokRepository(
    private val database: AokDatabase
) {
    val userDao = database.userDao()
    val themePreferenceDao = database.themePreferenceDao()

}