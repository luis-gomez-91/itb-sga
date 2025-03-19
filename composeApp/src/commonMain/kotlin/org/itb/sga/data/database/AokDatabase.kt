package org.itb.sga.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import org.itb.sga.data.entity.ParametroReporte
import org.itb.sga.data.entity.ThemePreference
import org.itb.sga.data.entity.User

const val DATABASE_NAME = "aok.db"

@Database(
    version = 3,
    entities = [
        User::class,
        ThemePreference::class,
        ParametroReporte::class
   ]
)
abstract class AokDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun themePreferenceDao(): ThemePreferenceDao
    abstract fun parametroReporteDao(): ParametroReporteDao
}