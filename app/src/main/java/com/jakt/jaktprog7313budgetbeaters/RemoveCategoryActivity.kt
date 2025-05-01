package com.jakt.jaktprog7313budgetbeaters

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jakt.jaktprog7313budgetbeaters.databinding.ActivityRemoveCategoryBinding
import kotlinx.coroutines.launch

class RemoveCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRemoveCategoryBinding
    private var selectedCategoryId: Int? = null
    private var categoryMap = mutableMapOf<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRemoveCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        loadCategories()
        setupButtons()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupBottomNav()
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            val database = AppDatabase.getDatabase(applicationContext)
            val categories = database.categoryDao().getAllCategories()

            // Create a map of category names to their IDs
            categoryMap = categories.associate { it.categoryName to it.id }.toMutableMap()

            val categoryNames = categoryMap.keys.toList()
            val adapter = ArrayAdapter(this@RemoveCategoryActivity, R.layout.spinner_item_white, categoryNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categoriesSpinner.adapter = adapter

            binding.categoriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val selectedName = categoryNames[position]
                    selectedCategoryId = categoryMap[selectedName]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    selectedCategoryId = null
                }
            }

        }
    }

    private fun setupButtons() {
        binding.ConfirmDelBtn.setOnClickListener {
            deleteSelectedCategory()
        }

        binding.CancelDelBtn.setOnClickListener {
            finish()
        }
    }

    private fun deleteSelectedCategory() {
        val id = selectedCategoryId
        if (id == null) {
            Toast.makeText(this, "No category selected", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val database = AppDatabase.getDatabase(applicationContext)
            database.categoryDao().deleteCategoriesByIds(listOf(id))
            Toast.makeText(this@RemoveCategoryActivity, "Category deleted", Toast.LENGTH_SHORT).show()
        }

        finish()
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
