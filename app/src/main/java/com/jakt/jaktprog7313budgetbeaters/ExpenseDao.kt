package com.jakt.jaktprog7313budgetbeaters

import androidx.room.*

@Dao
interface ExpenseDao {

    // Method to insert a new expense, aborting if there's a conflict
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertExpense(expense: ExpenseEntity)

    // Method to delete an expense by its ID
    @Query("DELETE FROM expenses WHERE id IN (:expenseIds)")
    suspend fun deleteExpensesByIds(expenseIds: List<Int>)

    // Method to retrieve all expenses, ordered by the name of the expense
    @Query("SELECT * FROM expenses ORDER BY name ASC")
    suspend fun getAllExpenses(): List<ExpenseEntity>

    // Method to retrieve an expense by its ID
    @Query("SELECT * FROM expenses WHERE id = :expenseId LIMIT 1")
    suspend fun getExpenseById(expenseId: Int): ExpenseEntity?

    // Method to update an existing expense
    @Update
    suspend fun updateExpense(expense: ExpenseEntity)

    // Method to delete all expenses
    @Query("DELETE FROM expenses")
    suspend fun deleteAllExpenses()

    // Additional method to delete a specific expense by name (optional)
    @Query("DELETE FROM expenses WHERE name = :expenseName")
    suspend fun deleteExpenseByName(expenseName: String)

    @Query("DELETE FROM expenses WHERE id = :expenseId")
    suspend fun deleteExpenseById(expenseId: Int)

    @Query("SELECT * FROM expenses WHERE date BETWEEN :start AND :end")
    suspend fun getExpensesByDateRange(start: String, end: String): List<ExpenseEntity>

    @Query("SELECT DISTINCT category FROM expenses")
    suspend fun getAllCategories(): List<String>

    @Query("""
        SELECT SUM(amount) FROM expenses 
        WHERE category = :category 
        AND date BETWEEN :startDate AND :endDate
    """)
    suspend fun getTotalSpentForCategoryInRange(
        category: String,
        startDate: String,
        endDate: String
    ): Double?
}
