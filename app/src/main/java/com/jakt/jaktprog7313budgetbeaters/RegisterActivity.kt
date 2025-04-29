//package com.jakt.jaktprog7313budgetbeaters
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import androidx.lifecycle.lifecycleScope
//import com.jakt.jaktprog7313budgetbeaters.databinding.ActivityRegisterBinding
//import kotlinx.coroutines.launch
//
//class RegisterActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityRegisterBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityRegisterBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        enableEdgeToEdge()
//
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        binding.SignUpBtn.setOnClickListener {
//            val name = binding.CreateNameInput.text.toString().trim()
//            val email = binding.CreateEmailInput.text.toString().trim()
//            val password = binding.CreatePasswordInput.text.toString().trim()
//            val confirmPassword = binding.confirmPasswordInput.text.toString().trim()
//
//            if (validateInput(name, email, password, confirmPassword)) {
//                lifecycleScope.launch {
//                    try {
//                        val database = AppDatabase.getDatabase(applicationContext)
//                        val existingUser = database.userDao().getUserByUsernameOrEmail(name, email)
//
//                        if (existingUser != null) {
//                            runOnUiThread {
//                                if (existingUser.username == name) {
//                                    binding.CreateNameInput.error = "Username already exists"
//                                }
//                                if (existingUser.email == email) {
//                                    binding.CreateEmailInput.error = "Email already registered"
//                                }
//                                Toast.makeText(
//                                    this@RegisterActivity,
//                                    "Registration failed: User exists",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        } else {
//                            database.userDao().insertUser(
//                                UserEntity(
//                                    username = name,
//                                    email = email,
//                                    password = password
//                                )
//                            )
//                            runOnUiThread {
//                                Toast.makeText(
//                                    this@RegisterActivity,
//                                    "Registration successful!",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                // Changed to navigate to MenuActivity
//                                startActivity(Intent(this@RegisterActivity, MenuActivity::class.java))
//                                finish()
//                            }
//                        }
//                    } catch (e: Exception) {
//                        runOnUiThread {
//                            Toast.makeText(
//                                this@RegisterActivity,
//                                "Registration failed: ${e.message}",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun validateInput(
//        name: String,
//        email: String,
//        password: String,
//        confirmPassword: String
//    ): Boolean {
//        var isValid = true
//
//        if (name.isEmpty()) {
//            binding.CreateNameInput.error = "Name required"
//            isValid = false
//        }
//
//        if (email.isEmpty()) {
//            binding.CreateEmailInput.error = "Email required"
//            isValid = false
//        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            binding.CreateEmailInput.error = "Invalid email format"
//            isValid = false
//        }
//
//        if (password.isEmpty()) {
//            binding.CreatePasswordInput.error = "Password required"
//            isValid = false
//        } else if (password.length < 6) {
//            binding.CreatePasswordInput.error = "Password must be at least 6 characters"
//            isValid = false
//        }
//
//        if (confirmPassword.isEmpty()) {
//            binding.confirmPasswordInput.error = "Confirm password required"
//            isValid = false
//        } else if (password != confirmPassword) {
//            binding.confirmPasswordInput.error = "Passwords don't match"
//            isValid = false
//        }
//
//        return isValid
//    }
//}

package com.jakt.jaktprog7313budgetbeaters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.jakt.jaktprog7313budgetbeaters.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.SignUpBtn.setOnClickListener {
            val name = binding.CreateNameInput.text.toString().trim()
            val email = binding.CreateEmailInput.text.toString().trim()
            val password = binding.CreatePasswordInput.text.toString().trim()
            val confirmPassword = binding.confirmPasswordInput.text.toString().trim()

            if (validateInput(name, email, password, confirmPassword)) {
                lifecycleScope.launch {
                    try {
                        val database = AppDatabase.getDatabase(applicationContext)
                        val existingUser = database.userDao().getUserByUsernameOrEmail(name, email)

                        if (existingUser != null) {
                            runOnUiThread {
                                if (existingUser.username == name) {
                                    binding.CreateNameInput.error = "Username already exists"
                                }
                                if (existingUser.email == email) {
                                    binding.CreateEmailInput.error = "Email already registered"
                                }
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Registration failed: User exists",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            database.userDao().insertUser(
                                UserEntity(
                                    username = name,
                                    email = email,
                                    password = password
                                )
                            )

                            val newUser = database.userDao().getUserByUsername(name)

                            runOnUiThread {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Registration successful!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                                with(sharedPref.edit()) {
                                    putString("logged_in_user", newUser?.username ?: "")
                                    apply()
                                }

                                startActivity(Intent(this@RegisterActivity, MenuActivity::class.java))
                                finish()
                            }
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registration failed: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun validateInput(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            binding.CreateNameInput.error = "Name required"
            isValid = false
        }

        if (email.isEmpty()) {
            binding.CreateEmailInput.error = "Email required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.CreateEmailInput.error = "Invalid email format"
            isValid = false
        }

        if (password.isEmpty()) {
            binding.CreatePasswordInput.error = "Password required"
            isValid = false
        } else if (password.length < 6) {
            binding.CreatePasswordInput.error = "Password must be at least 6 characters"
            isValid = false
        }

        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordInput.error = "Confirm password required"
            isValid = false
        } else if (password != confirmPassword) {
            binding.confirmPasswordInput.error = "Passwords don't match"
            isValid = false
        }

        return isValid
    }
}