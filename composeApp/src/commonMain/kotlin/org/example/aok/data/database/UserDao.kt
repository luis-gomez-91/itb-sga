package org.example.aok.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import org.example.aok.data.entity.User

@Dao
interface UserDao {
    @Upsert
    suspend fun upsert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM user")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM user ORDER BY id DESC LIMIT 1")
    suspend fun getLastUser(): User?

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(user: User)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(user: List<User>)
//
//    @Query("SELECT * FROM user")
//    fun getAllAsFlow(): Flow<List<User>>
//
//    @Query("SELECT COUNT(*) as count FROM user")
//    suspend fun count(): Int
//
//    @Query("SELECT * FROM user WHERE id in (:ids)")
//    suspend fun loadAll(ids: List<Long>): List<User>
//
//    @Query("SELECT * FROM user WHERE id in (:ids)")
//    suspend fun loadMapped(ids: List<Long>): Map<
//            @MapColumn(columnName = "id")
//            Long,
//            User,
//            >

}