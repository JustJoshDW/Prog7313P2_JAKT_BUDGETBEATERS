package com.jakt.jaktprog7313budgetbeaters

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shared_users")
data class SharedUserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val ownerUserId: Int,           // ID of the user who is sharing
    val sharedUserName: String,
    val sharedUserEmail: String
)