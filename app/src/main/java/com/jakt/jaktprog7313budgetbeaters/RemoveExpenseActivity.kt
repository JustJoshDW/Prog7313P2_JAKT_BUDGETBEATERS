package com.jakt.jaktprog7313budgetbeaters

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch

class RemoveExpenseActivity : AppCompatActivity() {
    private lateinit var adapter: ExpenseAdapter
    private lateinit var database: AppDatabase
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remove_expense)

        recyclerView = findViewById(R.id.expensesRecyclerView)
        database = AppDatabase.getDatabase(this)

        findViewById<Button>(R.id.Exit).setOnClickListener {
            finish()
        }

        setupRecyclerView()
        loadExpenses()
        setupBottomNav()
    }

    private fun setupRecyclerView() {
        adapter = ExpenseAdapter(emptyList()) { expense ->
            showDeleteConfirmationDialog(expense)
        }
        recyclerView.adapter = adapter
    }

    private fun loadExpenses() {
        lifecycleScope.launch {
            val expenses = database.expenseDao().getAllExpenses()
            runOnUiThread {
                adapter = ExpenseAdapter(expenses) { expense ->
                    showDeleteConfirmationDialog(expense)
                }
                recyclerView.adapter = adapter
            }
        }
    }

    private fun showDeleteConfirmationDialog(expense: ExpenseEntity) {
        AlertDialog.Builder(this)
            .setTitle("Delete Expense?")
            .setMessage(
                "Expense: ${expense.name}\n" +
                        "Amount: R${expense.amount}\n" +
                        "Date: ${expense.date}\n" +
                        "Category: ${expense.category}"
            )
            .setPositiveButton("Yes") { _, _ -> deleteExpense(expense) }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteExpense(expense: ExpenseEntity) {
        lifecycleScope.launch {
            database.expenseDao().deleteExpenseById(expense.id)
            runOnUiThread {
                loadExpenses()
                Toast.makeText(
                    this@RemoveExpenseActivity,
                    "Expense deleted successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupBottomNav() {
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.Logout -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, LogoutFragment())
                        .commit()
                    true
                }
                R.id.Menu -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, Menu_NavFragment())
                        .commit()
                    true
                }
                R.id.BudgetingGuides -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, BudgetingGuidesFragment())
                        .commit()
                    true
                }
                R.id.Awards -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, AwardsFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}
