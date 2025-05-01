package com.jakt.jaktprog7313budgetbeaters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jakt.jaktprog7313budgetbeaters.databinding.ActivityRemoveCategoryBinding
import kotlinx.coroutines.launch


class RemoveCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRemoveCategoryBinding
    private lateinit var categoryAdapter: CategoryAdapter
    private val selectedCategoryIds = mutableSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRemoveCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        setupRecyclerView()
        setupButtons()
        loadCategories()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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

    private fun setupRecyclerView() {
        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(this)
        categoryAdapter = CategoryAdapter(mutableListOf()) { selectedIds ->
            selectedCategoryIds.clear()
            selectedCategoryIds.addAll(selectedIds)
        }
        binding.categoriesRecyclerView.adapter = categoryAdapter
    }

    private fun setupButtons() {
        binding.ConfirmDelBtn.setOnClickListener {
            deleteSelectedCategories()
        }
        binding.CancelDelBtn.setOnClickListener {
            finish()
        }
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            val database = AppDatabase.getDatabase(applicationContext)
            val categories = database.categoryDao().getAllCategories()
            categoryAdapter.updateCategories(categories)
        }
    }

    private fun deleteSelectedCategories() {
        if (selectedCategoryIds.isEmpty()) {
            Toast.makeText(this, "No categories selected", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val database = AppDatabase.getDatabase(applicationContext)
            database.categoryDao().deleteCategoriesByIds(selectedCategoryIds.toList())
            loadCategories()
            Toast.makeText(
                this@RemoveCategoryActivity,
                "Deleted ${selectedCategoryIds.size} categories",
                Toast.LENGTH_SHORT
            ).show()
        }
        // Close the activity after deletion
        finish()
    }
}