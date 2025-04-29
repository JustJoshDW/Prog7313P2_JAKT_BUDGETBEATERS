package com.jakt.jaktprog7313budgetbeaters

import android.content.Intent
import android.os.Bundle
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

class ForgotPassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailInput = findViewById<EditText>(R.id.createEmailInput)
        val submitBtn = findViewById<Button>(R.id.submitBtn)
        val infoText = findViewById<TextView>(R.id.alreadyRegisteredtxt)

        submitBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "PLEASE ENTER YOUR EMAIL", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = AppDatabase.getDatabase(this)
            lifecycleScope.launch {
                val userDao = db.userDao()
                val user = userDao.getUserByEmail(email)

                runOnUiThread {
                    if (user != null) {
                        val intent = Intent(this@ForgotPassActivity, ResetPassword::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)
                    } else {
                        infoText.text = "NO USER FOUND WITH THIS EMAIL"
                        infoText.setTextColor(getColor(android.R.color.holo_red_light))
                    }
                }
            }
        }
    }
}