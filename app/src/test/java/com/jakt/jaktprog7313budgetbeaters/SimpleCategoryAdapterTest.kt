package com.jakt.jaktprog7313budgetbeaters

import org.junit.Assert.assertEquals
import org.junit.Test

class SimpleCategoryAdapterTest {

    @Test
    fun `adapter returns correct item count`() {
        val sampleCategories = listOf(
            CategoryEntity(1, "Food","Groceries" ,700, 250),
            CategoryEntity(2, "Transport", "Filling Petrol",300,150),
            CategoryEntity(3, "Entertainment", "Movies",250,75)
        )

        val adapter = SimpleCategoryAdapter(sampleCategories)

        assertEquals(3, adapter.itemCount)
    }
}
