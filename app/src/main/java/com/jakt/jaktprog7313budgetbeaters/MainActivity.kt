package com.jakt.jaktprog7313budgetbeaters

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jakt.jaktprog7313budgetbeaters.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var x1 = 0f
    private var x2 = 0f
    private val MIN_DISTANCE = 150

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainLayout = findViewById<ConstraintLayout>(R.id.main)

        mainLayout.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    x1 = event.x
                    true
                }
                MotionEvent.ACTION_UP -> {
                    x2 = event.x
                    val deltaX = x2 - x1
                    if (kotlin.math.abs(deltaX) > MIN_DISTANCE) {
                        if (x2 > x1) {
                            // Right swipe detected
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                        }
                    }
                    true
                }
                else -> false
            }
        }
    }
}
