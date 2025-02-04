package org.example.aok.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import org.example.aok.data.entity.User

//interface DB {
//    fun clearAllTables()
//}
//
//@Database(
//    entities = [User::class],
//    version = 1
//)
//
//abstract class AppDatabase : RoomDatabase(), DB{
//    abstract fun userDao(): UserDao
//
//    override fun clearAllTables() {}
//}

@Database(entities = [User::class], version = 1)
//@ConstructedBy(AppDatabaseFactory::class) // Necesario para KMM
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}