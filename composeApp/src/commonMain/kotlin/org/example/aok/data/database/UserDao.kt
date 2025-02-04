package org.example.aok.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.example.aok.data.entity.User

@Dao
interface UserDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(userPreferences: User)
//
//    @Query("SELECT * FROM user_preferences WHERE id = 0 LIMIT 1")
//    suspend fun getUserPreferences(): User?
//
//    @Query("SELECT * FROM user_preferences")
//    fun getUserPreferencesAll(): Flow<List<User>>?
//
//    @Update
//    suspend fun update(userPreferences: User)

    @Upsert
    suspend fun upsert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>
}