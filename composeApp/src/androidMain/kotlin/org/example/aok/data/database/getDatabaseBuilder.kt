package org.example.aok.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AokDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath("aok.db")
    return Room.databaseBuilder<AokDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}