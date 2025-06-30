package com.example.uas_ml2_221351089.ui.dataset

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_ml2_221351089.databinding.ItemDatasetColumnBinding

class DatasetColumnAdapter(
    private val columnTitles: List<String>,
    private val columnValues: List<List<String>>
) : RecyclerView.Adapter<DatasetColumnAdapter.ColumnViewHolder>() {

    inner class ColumnViewHolder(val binding: ItemDatasetColumnBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColumnViewHolder {
        val binding = ItemDatasetColumnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColumnViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColumnViewHolder, position: Int) {
        holder.binding.textTitle.text = columnTitles[position]
        holder.binding.textValues.text = columnValues[position].joinToString("\n")
    }

    override fun getItemCount(): Int = columnTitles.size
}
