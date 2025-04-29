package com.jakt.jaktprog7313budgetbeaters

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class SharedBudgetingActivity : AppCompatActivity() {
    private lateinit var membersContainer: LinearLayout
    private lateinit var memberCountInput: EditText
    private lateinit var submitButton: Button
    private lateinit var database: AppDatabase
    private var currentUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shared_budgeting)

        database = AppDatabase.getDatabase(this)
        membersContainer = findViewById(R.id.membersContainer)
        memberCountInput = findViewById(R.id.memberCountInput)
        submitButton = findViewById(R.id.submitButton)

        setupNumberInputListener()
        setupSubmitButton()
        loadCurrentUser()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadCurrentUser() {
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("logged_in_user", "") ?: ""

        lifecycleScope.launch {
            val user = database.userDao().getUserByUsername(username)
            if (user != null) {
                currentUserId = user.id
                loadExistingSharedUsers()
            } else {
                runOnUiThread {
                    Toast.makeText(
                        this@SharedBudgetingActivity,
                        "User not logged in!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }

    private fun loadExistingSharedUsers() {
        lifecycleScope.launch {
            val sharedUsers = database.sharedUserDao().getSharedUsersByOwner(currentUserId)
            runOnUiThread {
                if (sharedUsers.isNotEmpty()) {
                    memberCountInput.setText(sharedUsers.size.toString())
                    sharedUsers.forEachIndexed { index, sharedUser ->
                        addMemberInputFields(index + 1, sharedUser.sharedUserName, sharedUser.sharedUserEmail)
                    }
                }
            }
        }
    }

    private fun setupNumberInputListener() {
        memberCountInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateMemberFields()
            }
        })
    }

    private fun updateMemberFields() {
        val memberCount = memberCountInput.text.toString().toIntOrNull() ?: 0
        membersContainer.removeAllViews()

        if (memberCount > 0) {
            for (i in 1..memberCount) {
                addMemberInputFields(i)
            }
        }
    }

    private fun addMemberInputFields(memberNumber: Int, name: String = "", email: String = "") {
        val nameEditText = EditText(this).apply {
            hint = "Member $memberNumber Name"
            setText(name)
            setTextSize(16f)
            setTextColor(resources.getColor(android.R.color.white))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 16.dpToPx()
            }
        }

        val emailEditText = EditText(this).apply {
            hint = "Member $memberNumber Email"
            setText(email)
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            setTextSize(16f)
            setTextColor(resources.getColor(android.R.color.white))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 24.dpToPx()
            }
        }

        membersContainer.addView(nameEditText)
        membersContainer.addView(emailEditText)
    }

    private fun setupSubmitButton() {
        submitButton.setOnClickListener {
            if (currentUserId == -1) {
                Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val memberCount = memberCountInput.text.toString().toIntOrNull() ?: 0
            if (memberCount < 1) {
                Toast.makeText(this, "Please enter number of members", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val members = mutableListOf<Pair<String, String>>()
            for (i in 0 until membersContainer.childCount step 2) {
                val name = (membersContainer.getChildAt(i) as EditText).text.toString().trim()
                val email = (membersContainer.getChildAt(i + 1) as EditText).text.toString().trim()

                if (name.isEmpty() || email.isEmpty()) {
                    Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                members.add(Pair(name, email))
            }

            lifecycleScope.launch {
                database.sharedUserDao().deleteSharedUsersForOwner(currentUserId)

                members.forEach { (name, email) ->
                    database.sharedUserDao().insertSharedUser(
                        SharedUserEntity(
                            ownerUserId = currentUserId,
                            sharedUserName = name,
                            sharedUserEmail = email
                        )
                    )
                }

                runOnUiThread {
                    Toast.makeText(
                        this@SharedBudgetingActivity,
                        "Successfully shared with ${members.size} members!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun Int.dpToPx(): Int = (this * resources.displayMetrics.density).toInt()
}