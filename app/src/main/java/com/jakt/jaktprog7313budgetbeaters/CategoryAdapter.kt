package com.jakt.jaktprog7313budgetbeaters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.jakt.jaktprog7313budgetbeaters.R

class CategoryAdapter(
    private val categories: MutableList<CategoryEntity>,
    private val onSelectionChanged: (Set<Int>) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private val selectedIds = mutableSetOf<Int>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.categoryCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.checkBox.text = category.categoryName
        holder.checkBox.isChecked = selectedIds.contains(category.id)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedIds.add(category.id)
            } else {
                selectedIds.remove(category.id)
            }
            onSelectionChanged(selectedIds)
        }
    }

    override fun getItemCount() = categories.size

    fun updateCategories(newCategories: List<CategoryEntity>) {
        categories.clear()
        categories.addAll(newCategories)
        notifyDataSetChanged()
    }
}