package com.jakt.jaktprog7313budgetbeaters

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        // Handle edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }

        setupViews()
        setupDatePickers()
        setupRecyclerView()
        setupButtons()
        loadAllExpenses()

        // Bottom navigation setup
        findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            .setOnItemSelectedListener { item ->
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

    private fun setupViews() {
        fromDateInput = findViewById(R.id.FromDateInput)
        toDateInput   = findViewById(R.id.ToDateInput)
        recyclerView  = findViewById(R.id.expensesRecyclerView)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FilteredExpenseAdapter(emptyList())
        recyclerView.adapter = adapter
    }

    private fun setupDatePickers() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar   = Calendar.getInstance()

        val picker = { editText: EditText ->
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

        fromDateInput.setOnClickListener { picker(fromDateInput) }
        toDateInput.setOnClickListener   { picker(toDateInput)   }
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.submitBtn).setOnClickListener {
            val start = fromDateInput.text.toString()
            val end   = toDateInput.text.toString()

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
                val db = AppDatabase.getDatabase(applicationContext)
                allExpenses = db.expenseDao().getAllExpenses()
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
                val db         = AppDatabase.getDatabase(applicationContext)
                val expenses   = db.expenseDao().getAllExpenses()
                val inputFmt   = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val dbFmt      = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
                val startDate  = inputFmt.parse(start)!!
                val endDate    = inputFmt.parse(end)!!

                val filtered = expenses.filter { exp ->
                    try {
                        val expDate = dbFmt.parse(exp.date)!!
                        expDate.time in startDate.time..endDate.time
                    } catch (_: Exception) {
                        false
                    }
                }

                runOnUiThread {
                    if (filtered.isEmpty()) {
                        Toast.makeText(this@ViewExpenses, "No expenses in selected range", Toast.LENGTH_SHORT).show()
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
