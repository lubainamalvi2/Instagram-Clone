package com.example.mobileapplicationdevelopment2025.room

import androidx.room.*
import com.example.mobileapplicationdevelopment2025.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE id != '' LIMIT 1")
    suspend fun getCurrentUser(): User?
    
    @Query("DELETE FROM users WHERE id != :currentUserId")
    suspend fun clearOtherUsers(currentUserId: String)
}
