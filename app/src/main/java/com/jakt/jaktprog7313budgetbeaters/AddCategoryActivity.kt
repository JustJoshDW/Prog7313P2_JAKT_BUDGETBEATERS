package com.jakt.jaktprog7313budgetbeaters

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jakt.jaktprog7313budgetbeaters.databinding.ActivityAddCategoryBinding
import kotlinx.coroutines.launch

class AddCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        val db = AppDatabase.getDatabase(applicationContext)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.SaveBtn.setOnClickListener {
            val categoryName = binding.categoryNameInput.text.toString().trim()
            val description = binding.DescriptionInput.text.toString().trim()
            val maxLimitStr = binding.MaxLimitInput.text.toString().trim()
            val minLimitStr = binding.MinLimitInput.text.toString().trim()

            if (validateInput(categoryName, maxLimitStr, minLimitStr)) {
                val maxLimit = maxLimitStr.toInt()
                val minLimit = minLimitStr.toInt()

                lifecycleScope.launch {
                    try {
                        val database = AppDatabase.getDatabase(applicationContext)
                        database.categoryDao().insertCategory(
                            CategoryEntity(
                                categoryName = categoryName,
                                description = if (description.isNotEmpty()) description else null,
                                maxLimit = maxLimit,
                                minLimit = minLimit
                            )
                        )

                        runOnUiThread {
                            Toast.makeText(
                                this@AddCategoryActivity,
                                "Category saved successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(
                                this@AddCategoryActivity,
                                "Error saving category: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
        setupBottomNav()
    }

    private fun validateInput(categoryName: String, maxLimit: String, minLimit: String): Boolean {
        var isValid = true

        if (categoryName.isEmpty()) {
            binding.categoryNameInput.error = "Category name required"
            isValid = false
        }

        if (maxLimit.isEmpty()) {
            binding.MaxLimitInput.error = "Max goal is required"
            isValid = false
        } else if (!maxLimit.all { it.isDigit() }) {
            binding.MaxLimitInput.error = "Only numbers allowed"
            isValid = false
        }

        if (minLimit.isEmpty()) {
            binding.MinLimitInput.error = "Min goal is required"
            isValid = false
        } else if (!minLimit.all { it.isDigit() }) {
            binding.MinLimitInput.error = "Only numbers allowed"
            isValid = false
        }

        return isValid
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
