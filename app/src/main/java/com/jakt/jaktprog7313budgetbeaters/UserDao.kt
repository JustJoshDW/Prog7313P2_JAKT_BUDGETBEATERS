package com.jakt.jaktprog7313budgetbeaters

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE username = :username OR email = :email")
    suspend fun getUserByUsernameOrEmail(username: String, email: String): UserEntity?
}