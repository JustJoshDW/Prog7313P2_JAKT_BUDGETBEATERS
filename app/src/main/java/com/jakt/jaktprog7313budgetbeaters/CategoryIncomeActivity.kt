package com.jakt.jaktprog7313budgetbeaters

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class CategoryIncomeActivity : AppCompatActivity() {

    private lateinit var categorySpinner: Spinner
    private lateinit var fromDateInput: EditText
    private lateinit var toDateInput: EditText
    private lateinit var submitBtn: Button
    private lateinit var totalTextView: TextView

    private lateinit var db:AppDatabase

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_income) // Replace with your actual layout file name

        // Initialize views
        categorySpinner = findViewById(R.id.categorySpinner)
        fromDateInput = findViewById(R.id.FromDateInput)
        toDateInput = findViewById(R.id.ToDateInput)
        submitBtn = findViewById(R.id.submitBtn)
        totalTextView = findViewById(R.id.totalTextView)

        db = AppDatabase.getDatabase(this)

        loadCategories()

        fromDateInput.setOnClickListener { showDatePicker(fromDateInput) }
        toDateInput.setOnClickListener { showDatePicker(toDateInput) }

        submitBtn.setOnClickListener {
            handleSubmission()
        }

        setupBottomNav()
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            val categories = withContext(Dispatchers.IO) {
                db.expenseDao().getAllCategories()
            }

            val adapter = ArrayAdapter(
                this@CategoryIncomeActivity,
                android.R.layout.simple_spinner_item,
                categories.distinct().sorted()
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = adapter
        }
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, day)
            editText.setText(formattedDate)
        }

        DatePickerDialog(
            this, dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun handleSubmission() {
        val selectedCategory = categorySpinner.selectedItem?.toString() ?: return
        val fromDate = fromDateInput.text.toString()
        val toDate = toDateInput.text.toString()

        if (fromDate.isBlank() || toDate.isBlank()) {
            Toast.makeText(this, "Please select both dates.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val totalSpent = withContext(Dispatchers.IO) {
                db.expenseDao().getTotalSpentForCategoryInRange(selectedCategory, fromDate, toDate)
            }

            val display = totalSpent?.let { "Total Spent: R%.2f".format(it) } ?: "No expenses found for this period."
            totalTextView.text = display
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
