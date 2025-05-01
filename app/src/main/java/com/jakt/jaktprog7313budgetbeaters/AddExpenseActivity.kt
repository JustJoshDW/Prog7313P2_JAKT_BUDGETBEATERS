package com.jakt.jaktprog7313budgetbeaters

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.jakt.jaktprog7313budgetbeaters.databinding.ActivityAddExpenseBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddExpenseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddExpenseBinding
    private var selectedImageUri: Uri? = null // Changed to Uri type

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        // Handle insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load categories from database into spinner
        loadCategoriesFromDatabase()

        // Handle image upload
        binding.uploadImageView.setOnClickListener {
            openImagePicker()
        }

        // Save button functionality
        binding.SaveBtn.setOnClickListener {
            val expenseName = binding.EXPENSENameInput3.text.toString().trim()
            val category = binding.CATEGORYSpinner.selectedItem?.toString() ?: ""
            val date = binding.DATEInput.text.toString().trim()
            val amount = binding.EXPENSEInput3.text.toString().trim().toDoubleOrNull()
            val description = binding.EXPENSEDescriptionInput.text.toString().trim()

            if (validateInput(expenseName, category, date, amount, description)) {
                lifecycleScope.launch {
                    try {
                        val database = AppDatabase.getDatabase(applicationContext)
                        database.expenseDao().insertExpense(
                            ExpenseEntity(
                                name = expenseName,
                                category = category,
                                date = date,
                                amount = amount ?: 0.0,
                                description = if (description.isNotEmpty()) description else null,
                                imagePath = selectedImageUri?.toString() // Saving URI as string
                            )
                        )
                        runOnUiThread {
                            Toast.makeText(this@AddExpenseActivity, "Expense saved successfully!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(this@AddExpenseActivity, "Error saving expense: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        // Date picker
        binding.DATEInput.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                binding.DATEInput.setText(formattedDate)
            }, year, month, day)

            datePickerDialog.show()
        }
    }

    private fun loadCategoriesFromDatabase() {
        lifecycleScope.launch {
            try {
                val db = AppDatabase.getDatabase(applicationContext)
                val categoryNames = withContext(Dispatchers.IO) {
                    db.categoryDao().getAllCategories().map { it.categoryName }
                }

                if (categoryNames.isNotEmpty()) {
                    val adapter = ArrayAdapter(this@AddExpenseActivity, android.R.layout.simple_spinner_dropdown_item, categoryNames)
                    binding.CATEGORYSpinner.adapter = adapter
                } else {
                    Toast.makeText(this@AddExpenseActivity, "No categories found. Please add some first.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AddExpenseActivity, "Error loading categories: ${e.message}", Toast.LENGTH_SHORT).show()
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
            selectedImageUri = imageUri
            // Use Glide to load the image into the ImageView
            Glide.with(this)
                .load(imageUri)
                .into(binding.uploadImageView)
        }
    }

    private fun validateInput(expenseName: String, category: String, date: String, amount: Double?, description: String): Boolean {
        var isValid = true

        if (expenseName.isEmpty()) {
            binding.EXPENSENameInput3.error = "Expense name required"
            isValid = false
        } else binding.EXPENSENameInput3.error = null

        if (category.isEmpty() || category == "Select Category") {
            Toast.makeText(this, "Please select a category", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (date.isEmpty()) {
            binding.DATEInput.error = "Date required"
            isValid = false
        } else binding.DATEInput.error = null

        if (amount == null || amount <= 0) {
            binding.EXPENSEInput3.error = "Valid amount required"
            isValid = false
        } else binding.EXPENSEInput3.error = null

        if (description.isEmpty()) {
            binding.EXPENSEDescriptionInput.error = "Description required"
            isValid = false
        } else binding.EXPENSEDescriptionInput.error = null

        return isValid
    }

    companion object {
        const val IMAGE_PICK_CODE = 1000
    }
}
