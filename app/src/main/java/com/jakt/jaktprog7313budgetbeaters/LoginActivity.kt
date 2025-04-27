package com.jakt.jaktprog7313budgetbeaters

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.jakt.jaktprog7313budgetbeaters.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Login Button Click Listener (using ForgotPasswordBtn as login button)
        binding.ForgotPasswordBtn.setOnClickListener {
            val username = binding.LoginNameInput.text.toString().trim()
            val password = binding.PasswordInput.text.toString().trim()

            if (validateInput(username, password)) {
                authenticateUser(username, password)
            }
        }

        // Registration Button Click Listener
        binding.RegisterBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        var isValid = true

        if (username.isEmpty()) {
            binding.LoginNameInput.error = "Username required"
            isValid = false
        }

        if (password.isEmpty()) {
            binding.PasswordInput.error = "Password required"
            isValid = false
        }

        return isValid
    }

    private fun authenticateUser(username: String, password: String) {
        lifecycleScope.launch {
            try {
                val database = AppDatabase.getDatabase(applicationContext)
                val user = database.userDao().getUserByUsernameOrEmail(username, username)

                runOnUiThread {
                    if (user != null && user.password == password) {
                        handleSuccessfulLogin()
                    } else {
                        showLoginError()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@LoginActivity,
                        "Login error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun handleSuccessfulLogin() {
        Toast.makeText(
            this@LoginActivity,
            "Login successful!",
            Toast.LENGTH_SHORT
        ).show()
        startActivity(Intent(this@LoginActivity, MenuActivity::class.java))
        finish()
    }

    private fun showLoginError() {
        binding.PasswordInput.error = "Invalid credentials"
        Toast.makeText(
            this@LoginActivity,
            "Wrong username or password",
            Toast.LENGTH_SHORT
        ).show()
    }
}