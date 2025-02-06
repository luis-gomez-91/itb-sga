package org.example.aok.data.database

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.ktor.util.reflect.instanceOf
import platform.Foundation.NSHomeDirectory

fun getAokDatabase(): AokDatabase {
    val dbFile = NSHomeDirectory() + "/${DATABASE_NAME}"
    return Room.databaseBuilder<AokDatabase>(
        name = dbFile,
//        factory = { AokDatabase::class.instantiateImpl() }
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}