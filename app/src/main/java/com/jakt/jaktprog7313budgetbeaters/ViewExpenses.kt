package com.jakt.jaktprog7313budgetbeaters

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ViewExpenses : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FilteredExpenseAdapter
    private lateinit var fromDateInput: EditText
    private lateinit var toDateInput: EditText
    private var allExpenses = listOf<ExpenseEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_expenses)

        setupViews()
        setupDatePickers()
        setupRecyclerView()
        setupButtons()
        loadAllExpenses()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupViews() {
        fromDateInput = findViewById(R.id.FromDateInput)
        toDateInput = findViewById(R.id.ToDateInput)
        recyclerView = findViewById(R.id.expensesRecyclerView)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FilteredExpenseAdapter(emptyList())
        recyclerView.adapter = adapter
    }

    private fun setupDatePickers() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        val datePicker = { editText: EditText ->
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    editText.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        fromDateInput.setOnClickListener { datePicker(fromDateInput) }
        toDateInput.setOnClickListener { datePicker(toDateInput) }
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.submitBtn).setOnClickListener {
            val start = fromDateInput.text.toString()
            val end = toDateInput.text.toString()

            if (start.isEmpty() || end.isEmpty()) {
                Toast.makeText(this, "Please select both dates", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (start > end) {
                Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            filterExpenses(start, end)
        }

        findViewById<Button>(R.id.ViewAllBtn).setOnClickListener {
            showAllExpenses()
        }
    }

    private fun loadAllExpenses() {
        lifecycleScope.launch {
            try {
                val database = AppDatabase.getDatabase(applicationContext)
                allExpenses = database.expenseDao().getAllExpenses()
                runOnUiThread {
                    adapter = FilteredExpenseAdapter(allExpenses)
                    recyclerView.adapter = adapter
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@ViewExpenses,
                        "Error loading expenses: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun filterExpenses(start: String, end: String) {
        lifecycleScope.launch {
            try {
                // Get fresh data from database
                val database = AppDatabase.getDatabase(applicationContext)
                val allExpenses = database.expenseDao().getAllExpenses()

                // Create date formatters
                val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dbDateFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())

                // Parse filter dates
                val startDate = inputFormat.parse(start)!!
                val endDate = inputFormat.parse(end)!!

                // Filter expenses
                val filtered = allExpenses.filter { expense ->
                    try {
                        val expenseDate = dbDateFormat.parse(expense.date)!!
                        expenseDate.time in startDate.time..endDate.time
                    } catch (e: Exception) {
                        false
                    }
                }

                runOnUiThread {
                    if (filtered.isEmpty()) {
                        Toast.makeText(
                            this@ViewExpenses,
                            "No expenses in selected range",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    adapter = FilteredExpenseAdapter(filtered)
                    recyclerView.adapter = adapter
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@ViewExpenses,
                        "Error filtering expenses: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun showAllExpenses() {
        fromDateInput.text.clear()
        toDateInput.text.clear()
        adapter = FilteredExpenseAdapter(allExpenses)
        recyclerView.adapter = adapter
    }
}