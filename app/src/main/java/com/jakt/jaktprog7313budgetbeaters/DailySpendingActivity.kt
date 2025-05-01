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

        setupBottomNav()
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
