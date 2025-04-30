package com.jakt.jaktprog7313budgetbeaters

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CategoryDao {
//    @Insert(onConflict = OnConflictStrategy.ABORT)
//    suspend fun insertCategory(category: CategoryEntity)
//
//    @Query("SELECT * FROM categories ORDER BY categoryName ASC")
//    suspend fun getAllCategories(): List<CategoryEntity>
//
//    @Query("DELETE FROM categories WHERE id = :categoryId")
//    suspend fun deleteCategory(categoryId: Int)

    @Query("DELETE FROM categories WHERE id IN (:categoryIds)")
    suspend fun deleteCategoriesByIds(categoryIds: List<Int>)

    // Keep other existing methods
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCategory(category: CategoryEntity)

    @Query("SELECT * FROM categories ORDER BY categoryName ASC")
    suspend fun getAllCategories(): List<CategoryEntity>

}