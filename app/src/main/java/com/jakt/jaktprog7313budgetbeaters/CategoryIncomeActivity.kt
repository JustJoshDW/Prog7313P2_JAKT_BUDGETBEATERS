package com.jakt.jaktprog7313budgetbeaters

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import android.app.DatePickerDialog
import java.util.Calendar
import com.jakt.jaktprog7313budgetbeaters.AppDatabase
import com.jakt.jaktprog7313budgetbeaters.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryIncomeActivity : AppCompatActivity() {

    private lateinit var categorySpinner: Spinner
    private lateinit var db: AppDatabase  // Your Room database

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_income)

        categorySpinner = findViewById(R.id.categorySpinner)
        val fromDateInput = findViewById<EditText>(R.id.FromDateInput)
        val toDateInput = findViewById<EditText>(R.id.ToDateInput)
        db = AppDatabase.getDatabase(this)

        loadCategories()

        fromDateInput.setOnClickListener { showDatePicker(fromDateInput) }
        toDateInput.setOnClickListener { showDatePicker(toDateInput) }

    }

    private fun loadCategories() {
        lifecycleScope.launch {
            val categories = withContext(Dispatchers.IO) {
                db.expenseDao().getAllCategories()
            }

            val uniqueCategories = categories.distinct().sorted()
            val adapter = ArrayAdapter(
                this@CategoryIncomeActivity,
                android.R.layout.simple_spinner_item,
                uniqueCategories
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

}
