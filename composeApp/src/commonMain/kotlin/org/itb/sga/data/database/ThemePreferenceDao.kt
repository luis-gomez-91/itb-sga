package org.itb.sga.data.database

import androidx.room.Dao
import org.itb.sga.data.entity.ThemePreference
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ThemePreferenceDao {
    @Upsert
    suspend fun upsert(themePreference: ThemePreference)

    @Query("SELECT * FROM theme_preference ORDER BY id DESC LIMIT 1")
    suspend fun getLast(): ThemePreference?

    @Query("SELECT * FROM theme_preference")
    fun getAll(): Flow<List<ThemePreference>>

    @Query("DELETE FROM theme_preference")
    suspend fun delete()

    @Query("SELECT * FROM theme_preference WHERE active IS TRUE")
    suspend fun getSelected(): ThemePreference?

    @Query("UPDATE theme_preference SET active = false")
    suspend fun deactivateAll()


}