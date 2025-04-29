package com.jakt.jaktprog7313budgetbeaters

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ResetPassword : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val newPasswordInput = findViewById<EditText>(R.id.newPassword)
        val resetBtn = findViewById<Button>(R.id.resetBtn)
        val successMessage = findViewById<TextView>(R.id.successMessage)

        val email = intent.getStringExtra("email") ?: return

        resetBtn.setOnClickListener {
            val newPassword = newPasswordInput.text.toString().trim()

            if (newPassword.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = AppDatabase.getDatabase(this)
            lifecycleScope.launch {
                val userDao = db.userDao()
                val user = userDao.getUserByEmail(email)

                if (user != null) {
                    val updatedUser = user.copy(password = newPassword)
                    userDao.updateUser(updatedUser)

                    runOnUiThread {
                        successMessage.visibility = View.VISIBLE
                        Toast.makeText(this@ResetPassword, "Password reset successful", Toast.LENGTH_SHORT).show()

                        // Optional: Go to login screen after 2 seconds
                        resetBtn.postDelayed({
                            val intent = Intent(this@ResetPassword, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }, 2000)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@ResetPassword, "User not found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}