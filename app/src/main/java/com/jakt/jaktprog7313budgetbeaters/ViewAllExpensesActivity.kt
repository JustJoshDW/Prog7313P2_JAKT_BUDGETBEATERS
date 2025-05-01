package com.jakt.jaktprog7313budgetbeaters

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jakt.jaktprog7313budgetbeaters.databinding.ActivityViewAllExpensesBinding

class ViewAllExpensesActivity : AppCompatActivity() {

    // Declare the binding variable
    private lateinit var binding: ActivityViewAllExpensesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the binding
        binding = ActivityViewAllExpensesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Enable edge-to-edge display
        enableEdgeToEdge()

        // Set padding for the system bars
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

        // Set up the "Save" button click listener
        binding.AddExpenseBtn.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        binding.RemoveExpenseBtn.setOnClickListener{
            startActivity(Intent(this, RemoveExpenseActivity::class.java))
        }

        binding.CatIncomeBtn.setOnClickListener{
            startActivity(Intent(this, CategoryIncomeActivity::class.java))
        }

        binding.ViewExpenseBtn.setOnClickListener{
            startActivity(Intent(this, ViewExpenses::class.java))
        }
    }
}
