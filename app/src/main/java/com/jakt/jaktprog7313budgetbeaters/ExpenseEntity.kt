
package com.jakt.jaktprog7313budgetbeaters

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String,
    val date: String,
    val amount: Double,
    val description: String?,
    val imagePath: String? // This field stores the image file path
)
