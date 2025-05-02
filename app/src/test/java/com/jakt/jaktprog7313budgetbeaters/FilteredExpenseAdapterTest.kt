package com.jakt.jaktprog7313budgetbeaters

import org.junit.Assert.assertEquals
import org.junit.Test

class FilteredExpenseAdapterTest {

    @Test
    fun `adapter returns correct item count`() {
        val sampleData = listOf(
            ExpenseEntity(1, "Vegetables", "Groceries", "2024-05-01",120.0, "Food", null),
            ExpenseEntity(2, "Taxi", "Transportation", "2024-05-02", 80.0, "Transport", null),
            ExpenseEntity(3, "Movie", "Entertainment", "2024-05-03", 50.0, "Entertainment", null)
        )

        val adapter = FilteredExpenseAdapter(sampleData)

        assertEquals(3, adapter.itemCount)
    }
}
