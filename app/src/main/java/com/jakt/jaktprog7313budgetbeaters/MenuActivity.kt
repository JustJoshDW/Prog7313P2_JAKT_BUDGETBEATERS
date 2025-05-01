package com.jakt.jaktprog7313budgetbeaters

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jakt.jaktprog7313budgetbeaters.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Launch other activities from buttons
        binding.viewPieChartBtn.setOnClickListener {
            startActivity(Intent(this, PieChartActivity::class.java))
        }
        binding.viewAllExpensesBtn.setOnClickListener {
            startActivity(Intent(this, ViewAllExpensesActivity::class.java))
        }
        binding.viewDailySpendingBtn.setOnClickListener {

            startActivity(Intent(this, DailySpendingActivity::class.java))
        }


        binding.viewProgressDashboardBtn.setOnClickListener {
 
            startActivity(Intent(this, ProgressDashboardActivity::class.java))
        }
        binding.sharedBudgetingBtn.setOnClickListener {
            startActivity(Intent(this, SharedBudgetingActivity::class.java))
        }

        binding.categoriesBtn.setOnClickListener {
            startActivity(Intent(this, CategoriesActivity::class.java))
        }

        // Set up the BottomNavigationView to handle fragment changes
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
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
}