package com.jakt.jaktprog7313budgetbeaters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FilteredExpenseAdapter(
    private val expenses: List<ExpenseEntity>
) : RecyclerView.Adapter<FilteredExpenseAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView       = itemView.findViewById(R.id.expenseName)
        val amount: TextView     = itemView.findViewById(R.id.expenseAmount)
        val date: TextView       = itemView.findViewById(R.id.expenseDate)
        val image: ImageView     = itemView.findViewById(R.id.expenseImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expense = expenses[position]
        holder.name.text   = expense.name
        holder.amount.text = "R%.2f".format(expense.amount)
        holder.date.text   = expense.date

        if (!expense.imagePath.isNullOrEmpty()) {
            holder.image.visibility = View.VISIBLE
            Glide.with(holder.itemView.context)
                .load(Uri.parse(expense.imagePath))
                .into(holder.image)
        } else {
            holder.image.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = expenses.size
}
