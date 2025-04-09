package org.itb.sga.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import org.itb.sga.data.entity.ParametroReporte
import org.itb.sga.data.entity.ThemePreference
import org.itb.sga.data.entity.User

@Database(
    version = 3,
    exportSchema = true,
    entities = [
        User::class,
        ThemePreference::class,
        ParametroReporte::class
    ]
)

@ConstructedBy(AppDatabaseConstructor::class)
abstract class AokDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun themePreferenceDao(): ThemePreferenceDao
    abstract fun parametroReporteDao(): ParametroReporteDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor: RoomDatabaseConstructor<AokDatabase> {
    override fun initialize(): AokDatabase
}