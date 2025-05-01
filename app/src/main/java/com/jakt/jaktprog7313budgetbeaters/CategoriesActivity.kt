package com.jakt.jaktprog7313budgetbeaters

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class CategoriesActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SimpleCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)
        enableEdgeToEdge()

        setupRecyclerView()
        loadCategories()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.categoriesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SimpleCategoryAdapter(emptyList())
        recyclerView.adapter = adapter
    }

    private fun loadCategories() {
        lifecycleScope.launch {
            val database = AppDatabase.getDatabase(applicationContext)
            val categories = database.categoryDao().getAllCategories()
            runOnUiThread {
                adapter = SimpleCategoryAdapter(categories)
                recyclerView.adapter = adapter
            }
        }
    }
}