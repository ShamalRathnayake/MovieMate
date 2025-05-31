package com.shamalrathnayake.moviemate.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.shamalrathnayake.moviemate.R

class CategoryChipsAdapter(
    private val categories: List<Pair<String, String>>,
    private val onCategorySelected: (String) -> Unit
) : RecyclerView.Adapter<CategoryChipsAdapter.CategoryViewHolder>() {


    private var selectedPosition = 0


    init {
        selectedPosition = categories.indexOfFirst { it.second == "now_playing" }.coerceAtLeast(0)
    }

    fun getCurrentCategory(): String {
        return categories[selectedPosition].second
    }


    inner class CategoryViewHolder(val chip: Chip) : RecyclerView.ViewHolder(chip) {
        fun bind(category: Pair<String, String>, position: Int) {
            chip.text = category.first
            chip.isChecked = position == selectedPosition
            chip.setChipIconVisible(position == selectedPosition)
            chip.setOnClickListener {
                val previousSelected = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)
                onCategorySelected(category.second)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val chip = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_filter_chip, parent, false) as Chip
        return CategoryViewHolder(chip)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val (title, value) = categories[position]
        holder.chip.text = title
        holder.chip.isChecked = position == selectedPosition

        holder.chip.setOnClickListener {
            val previousSelected = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousSelected)
            notifyItemChanged(selectedPosition)
            onCategorySelected(categories[position].second)
        }
    }

}