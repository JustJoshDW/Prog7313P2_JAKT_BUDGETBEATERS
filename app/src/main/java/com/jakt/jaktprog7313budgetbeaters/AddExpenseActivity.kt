package com.jakt.jaktprog7313budgetbeaters

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.jakt.jaktprog7313budgetbeaters.databinding.ActivityAddExpenseBinding

class AddExpenseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddExpenseBinding
    private var selectedImageUri: Uri? = null // Changed to Uri type
    private val STORAGE_PERMISSION_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // On click listener for the ImageView to trigger image picker
        binding.uploadImageView.setOnClickListener {
            openImagePicker()
        }

        // On click listener for the save button
        binding.SaveBtn.setOnClickListener {
            // Your save logic here
            if (selectedImageUri != null) {
                // You can save or use the selected image URI here
                Toast.makeText(this, "Expense saved with image", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to open the image picker if permission is granted
    private fun openImagePicker() {
        if (checkStoragePermission()) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK_CODE)
        } else {
            requestStoragePermission()
        }
    }

    // Function to check if storage permission is granted
    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    // Function to request storage permission if not granted
    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
    }

    // Handling the result of permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Handling the result of the image picker activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val imageUri = data?.data
            selectedImageUri = imageUri
            Glide.with(this)
                .load(imageUri)
                .into(binding.uploadImageView)  // Loading the selected image into the ImageView
        }
    }

    companion object {
        const val IMAGE_PICK_CODE = 1000  // Request code for image picker
    }
}
