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


        binding.viewPieChartBtn.setOnClickListener {
            startActivity(Intent(this, PieChartActivity::class.java))
        }

        binding.viewAllExpensesBtn.setOnClickListener {
            startActivity(Intent(this, ViewAllExpensesActivity::class.java))
        }

        binding.viewDailySpendingBtn.setOnClickListener {
            startActivity(Intent(this, ViewAllSpendingActivity::class.java))
        }

        binding.viewProgressDashboardBtn.setOnClickListener {
            startActivity(Intent(this, ProgressDashboardActivity::class.java))
        }

        binding.sharedBudgetingBtn.setOnClickListener {
            startActivity(Intent(this, SharedBudgetingActivity::class.java))
        }
    }
}