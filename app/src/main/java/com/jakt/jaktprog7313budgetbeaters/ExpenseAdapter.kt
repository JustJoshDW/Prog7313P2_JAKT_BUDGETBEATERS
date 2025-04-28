package com.jakt.jaktprog7313budgetbeaters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(
    private val expenses: List<ExpenseEntity>,
    private val onExpenseSelected: (ExpenseEntity) -> Unit
) : RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.expenseName)
        val amount: TextView = itemView.findViewById(R.id.expenseAmount)
        val date: TextView = itemView.findViewById(R.id.expenseDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expense = expenses[position]
        holder.name.text = expense.name
        holder.amount.text = "R${expense.amount}"
        holder.date.text = expense.date

        holder.itemView.setOnClickListener {
            onExpenseSelected(expense)
        }
    }

    override fun getItemCount() = expenses.size
}