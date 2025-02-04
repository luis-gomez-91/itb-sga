package org.example.aok.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.example.aok.data.entity.User

const val DATABASE_NAME = "aok.db"

@Database(
    version = 1,
    entities = [
        User::class
   ]
)
abstract class AokDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}