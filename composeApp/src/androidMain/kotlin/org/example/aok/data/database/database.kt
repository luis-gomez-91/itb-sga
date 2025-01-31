package org.example.aok.data.database

//import android.content.Context
//import androidx.room.Room
//import androidx.sqlite.driver.bundled.BundledSQLiteDriver
//
//fun getDatabaseBuilder(context: Context): AppDatabase {
//    val dbFile = context.getDatabasePath(DATABSE_NAME)
//    return Room.databaseBuilder<AppDatabase>(
//        context = context.applicationContext,
//        name = dbFile.absolutePath
//    )
//        .setDriver(BundledSQLiteDriver())
//        .build()
//}