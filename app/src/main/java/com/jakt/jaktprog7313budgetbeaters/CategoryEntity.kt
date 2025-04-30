// CategoryEntity.kt
package com.jakt.jaktprog7313budgetbeaters

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val categoryName: String,
    val description: String? = null,
    val maxLimit: Int,
    val minLimit: Int
)