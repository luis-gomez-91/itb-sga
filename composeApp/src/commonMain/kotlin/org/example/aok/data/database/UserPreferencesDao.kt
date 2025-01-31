package org.example.aok.data.database

//import androidx.room.Dao
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import androidx.room.Update
//import kotlinx.coroutines.flow.Flow
//import org.example.aok.data.entity.UserPreferences
//
//@Dao
//interface UserPreferencesDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(userPreferences: UserPreferences)
//
//    @Query("SELECT * FROM user_preferences WHERE id = 0 LIMIT 1")
//    suspend fun getUserPreferences(): UserPreferences?
//
//    @Query("SELECT * FROM user_preferences")
//    fun getUserPreferencesAll(): Flow<List<UserPreferences>>?
//
//    @Update
//    suspend fun update(userPreferences: UserPreferences)
//}