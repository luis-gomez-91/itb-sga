package org.example.aok.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.example.aok.data.entity.ThemePreference
import org.example.aok.data.entity.User

const val DATABASE_NAME = "aok.db"

@Database(
    version = 2,
    entities = [
        User::class,
        ThemePreference::class
   ]
)
abstract class AokDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun themePreferenceDao(): ThemePreferenceDao
}