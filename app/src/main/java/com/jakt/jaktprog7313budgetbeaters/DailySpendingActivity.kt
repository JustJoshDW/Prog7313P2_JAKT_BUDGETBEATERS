package com.jakt.jaktprog7313budgetbeaters

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jakt.jaktprog7313budgetbeaters.databinding.ActivityDailySpendingBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DailySpendingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDailySpendingBinding
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailySpendingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDatePickers()
        setupButtons()

        // Set up the BottomNavigationView to handle fragment changes
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnItemSelectedListener { item ->
            when (item.itemId) {
                // Logout fragment
                R.id.Logout -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, LogoutFragment())
                        .commit()
                    true
                }

                // Menu fragment (to show the menu UI when clicked)
                R.id.Menu -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, Menu_NavFragment()) // Make sure MenuFragment is created
                        .commit()
                    true
                }

                // Budgeting Guides fragment
                R.id.BudgetingGuides -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, BudgetingGuidesFragment()) // Budgeting Guides fragment
                        .commit()
                    true
                }

                // Awards fragment
                R.id.Awards -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, AwardsFragment()) // Awards fragment
                        .commit()
                    true
                }

                // Default case if any item is selected that we don't have defined
                else -> false
            }
        }
    }

    private fun setupDatePickers() {
        val startCalendar = Calendar.getInstance()
        val endCalendar = Calendar.getInstance()

        binding.fromDateInput.setOnClickListener {
            DatePickerDialog(this, { _, y, m, d ->
                startCalendar.set(y, m, d)
                binding.fromDateInput.setText(dateFormatter.format(startCalendar.time))
            }, startCalendar.get(Calendar.YEAR), startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.toDateInput.setOnClickListener {
            DatePickerDialog(this, { _, y, m, d ->
                endCalendar.set(y, m, d)
                binding.toDateInput.setText(dateFormatter.format(endCalendar.time))
            }, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupButtons() {
        val startDateInput = binding.fromDateInput
        val endDateInput = binding.toDateInput
        binding.submitBtn.setOnClickListener {
            val start = binding.fromDateInput.text.toString()
            val end = binding.toDateInput.text.toString()

            if (start.isEmpty() || end.isEmpty()) {
                Toast.makeText(this, "Please select both dates", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dateFormatter.parse(start)!!.after(dateFormatter.parse(end))) {
                Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Intent(this, ViewAllSpendingActivity::class.java).apply {
                putExtra("START_DATE", start)
                putExtra("END_DATE", end)
                startActivity(this)
            }
        }

        binding.ViewAllBtn.setOnClickListener {
            startActivity(Intent(this, ViewAllSpendingActivity::class.java))
        }
    }
}