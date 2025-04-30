package com.jakt.jaktprog7313budgetbeaters

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.jakt.jaktprog7313budgetbeaters.databinding.ActivityViewAllSpendingBinding
import kotlinx.coroutines.launch

class ViewAllSpendingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewAllSpendingBinding
    private lateinit var barChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAllSpendingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        barChart = binding.barChart
        setupChartAppearance()
        loadData()
    }

    private fun setupChartAppearance() {
        with(barChart) {
            description.isEnabled = false
            setDrawGridBackground(false)
            setTouchEnabled(true)
            setPinchZoom(true)
            isDragEnabled = true
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            axisRight.isEnabled = false
        }
    }

    private fun loadData() {

        val start = intent.getStringExtra("START_DATE").orEmpty()
        val end   = intent.getStringExtra("END_DATE").orEmpty()
        Log.d("ViewAllSpending", "START=$start  END=$end")

        lifecycleScope.launch {
            val database = AppDatabase.getDatabase(applicationContext)
            val expenses = try {
                if (start.isNotBlank() && end.isNotBlank()) {
                    database.expenseDao().getExpensesByDateRange(start, end)
                } else {
                    database.expenseDao().getAllExpenses()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@ViewAllSpendingActivity,
                        "Error loading data: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@launch
            }

            runOnUiThread {
                if (expenses.isEmpty()) {
                    Toast.makeText(
                        this@ViewAllSpendingActivity,
                        "No expenses found in that range",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    setupBarChart(groupByCategory(expenses))
                }
            }
        }
    }

    private fun groupByCategory(expenses: List<ExpenseEntity>) =
        expenses.groupBy { it.category }
            .mapValues { it.value.sumOf { exp -> exp.amount } }

    private fun setupBarChart(categoryMap: Map<String, Double>) {
        val entries = categoryMap.entries.mapIndexed { i, (cat, total) ->
            BarEntry(i.toFloat(), total.toFloat())
        }
        val labels = categoryMap.keys.toList()

        val dataSet = BarDataSet(entries, "Expenses by Category").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
            valueTextColor = android.graphics.Color.BLACK
            valueTextSize = 12f
        }

        barChart.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            labelCount = labels.size
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
        }

        barChart.data = BarData(dataSet)
        barChart.animateY(1000)
        barChart.invalidate()
    }
}
