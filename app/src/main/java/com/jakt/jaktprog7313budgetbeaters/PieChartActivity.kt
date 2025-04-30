package com.jakt.jaktprog7313budgetbeaters

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PieChartActivity : AppCompatActivity() {

    private lateinit var pieChart: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pie_chart)

        pieChart = findViewById(R.id.pieChart)

        findViewById<Button>(R.id.btnAddCategory).setOnClickListener {
            startActivity(Intent(this, AddCategoryActivity::class.java))
        }

        findViewById<Button>(R.id.btnDeleteCategory).setOnClickListener {
            startActivity(Intent(this, RemoveCategoryActivity::class.java))
        }

        findViewById<Button>(R.id.backBtn).setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        setupPieChart() // Refresh chart when returning to this activity
    }

    private fun setupPieChart() {
        val db = AppDatabase.getDatabase(this)

        lifecycleScope.launch {
            val categories = withContext(Dispatchers.IO) {
                db.categoryDao().getAllCategories()
            }

            if (categories.isNotEmpty()) {
                val entries = categories.map {
                    PieEntry(it.maxLimit.toFloat(), it.categoryName)
                }

                // Remove the "Category Budgets" label
                val dataSet = PieDataSet(entries, "").apply { // Empty string to avoid the dataset title
                    colors = ColorTemplate.MATERIAL_COLORS.toList()
                }

                val data = PieData(dataSet)
                pieChart.data = data

                pieChart.apply {
                    isDrawHoleEnabled = true
                    holeRadius = 58f
                    setTransparentCircleRadius(61f)
                    animateY(1400)
                    description.isEnabled = false
                    invalidate() // Refresh the chart with new data

                    // Position the legend above the chart
                    legend.apply {
                        isEnabled = true
                        textSize = 14f // Text size of legend entries
                        formSize = 18f // Size of the colored box next to text
                        formToTextSpace = 10f

                        // Align the legend above the pie chart
                        verticalAlignment = Legend.LegendVerticalAlignment.TOP
                        horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                        orientation = Legend.LegendOrientation.HORIZONTAL // Change to vertical for multiple rows

                        // Add spacing between legend and chart
                        yOffset = 40f // Adjusted for better spacing
                        setDrawInside(false) // Prevent legend from drawing inside the chart
                    }

                    layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
                        topMargin = 20 // Add margin at the top to avoid overlap
                    }
                }
            } else {
                pieChart.clear() // Show empty state if no data
                pieChart.invalidate()
            }
        }
    }
}
