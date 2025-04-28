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
import com.jakt.jaktprog7313budgetbeaters.databinding.ActivityAddExpenseBinding
import kotlinx.coroutines.launch

class AddExpenseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddExpenseBinding
    private var selectedImageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // Set up window insets for edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Handle image upload
        binding.uploadImageView.setOnClickListener {
            openImagePicker()
        }

        // Handle saving expense data
        binding.SaveBtn.setOnClickListener {
            val expenseName = binding.EXPENSENameInput3.text.toString().trim()
            val category = binding.CATEGORYInput.text.toString().trim()
            val date = binding.DATEInput.text.toString().trim()
            val amount = binding.EXPENSEInput3.text.toString().trim().toDoubleOrNull()
            val description = binding.EXPENSEDescriptionInput.text.toString().trim()

            // Validate input fields
            if (validateInput(expenseName, category, date, amount, description)) {
                lifecycleScope.launch {
                    try {
                        val database = AppDatabase.getDatabase(applicationContext)
                        database.expenseDao().insertExpense(
                            ExpenseEntity(
                                name = expenseName,
                                category = category,
                                date = date,
                                amount = amount ?: 0.0, // Default to 0 if not provided
                                description = if (description.isNotEmpty()) description else null,
                                imagePath = selectedImageUri // Save image URI if available
                            )
                        )

                        runOnUiThread {
                            Toast.makeText(
                                this@AddExpenseActivity,
                                "Expense saved successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()  // Close activity after saving
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(
                                this@AddExpenseActivity,
                                "Error saving expense: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val imageUri = data?.data
            selectedImageUri = imageUri?.toString() // Convert URI to String for storage
            binding.uploadImageView.setImageURI(imageUri) // Display the image
        }
    }

    private fun validateInput(expenseName: String, category: String, date: String, amount: Double?, description: String): Boolean {
        var isValid = true

        // Validate expense name
        if (expenseName.isEmpty()) {
            binding.EXPENSENameInput3.error = "Expense name required"
            isValid = false
        } else {
            binding.EXPENSENameInput3.error = null
        }

        // Validate category
        if (category.isEmpty()) {
            binding.CATEGORYInput.error = "Category required"
            isValid = false
        } else {
            binding.CATEGORYInput.error = null
        }

        // Validate date
        if (date.isEmpty()) {
            binding.DATEInput.error = "Date required"
            isValid = false
        } else {
            binding.DATEInput.error = null
        }

        // Validate amount
        if (amount == null || amount <= 0) {
            binding.EXPENSEInput3.error = "Valid amount required"
            isValid = false
        } else {
            binding.EXPENSEInput3.error = null
        }

        // Validate description
        if (description.isEmpty()) {
            binding.EXPENSEDescriptionInput.error = "Description required"
            isValid = false
        } else {
            binding.EXPENSEDescriptionInput.error = null
        }

        return isValid
    }

    companion object {
        const val IMAGE_PICK_CODE = 1000
    }
}
