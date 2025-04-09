package org.itb.sga.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.itb.sga.core.DATABASE_NAME

fun getDatabaseBuilder(ctx: Context): RoomDatabase.Builder<AokDatabase> {
    val appContext = ctx.applicationContext
    val dbFile = appContext.getDatabasePath(DATABASE_NAME)
    return Room.databaseBuilder<AokDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}