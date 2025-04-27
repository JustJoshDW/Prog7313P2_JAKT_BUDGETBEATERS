package com.jakt.jaktprog7313budgetbeaters

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.jakt.jaktprog7313budgetbeaters.databinding.ActivityAddCategoryBinding
import kotlinx.coroutines.launch

class AddCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddCategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.SaveBtn.setOnClickListener {
            val categoryName = binding.categoryNameInput.text.toString().trim()
            val description = binding.categoryDescriptionInput.text.toString().trim()

            if (validateInput(categoryName)) {
                lifecycleScope.launch {
                    try {
                        val database = AppDatabase.getDatabase(applicationContext)
                        database.categoryDao().insertCategory(
                            CategoryEntity(
                                categoryName = categoryName,
                                description = if (description.isNotEmpty()) description else null
                            )
                        )

                        runOnUiThread {
                            Toast.makeText(
                                this@AddCategoryActivity,
                                "Category saved successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()  // Close the activity after saving
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
    }

    private fun validateInput(categoryName: String): Boolean {
        var isValid = true

        if (categoryName.isEmpty()) {
            binding.categoryNameInput.error = "Category name required"
            isValid = false
        } else {
            binding.categoryNameInput.error = null
        }

        return isValid
    }
}