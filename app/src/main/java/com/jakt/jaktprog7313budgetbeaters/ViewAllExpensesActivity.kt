package com.jakt.jaktprog7313budgetbeaters

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        // Set up the "Save" button click listener
        //binding.saveBtn2.setOnClickListener {
          //  startActivity(Intent(this, AddExpenseActivity::class.java))
        //}
    }
}
