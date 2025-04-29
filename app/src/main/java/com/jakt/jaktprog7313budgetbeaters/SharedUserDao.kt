package com.jakt.jaktprog7313budgetbeaters

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SharedUserDao {
    @Insert
    suspend fun insertSharedUser(sharedUser: SharedUserEntity)

    @Query("SELECT * FROM shared_users WHERE ownerUserId = :ownerId")
    suspend fun getSharedUsersByOwner(ownerId: Int): List<SharedUserEntity>

    @Query("DELETE FROM shared_users WHERE ownerUserId = :ownerId")
    suspend fun deleteSharedUsersForOwner(ownerId: Int)
}