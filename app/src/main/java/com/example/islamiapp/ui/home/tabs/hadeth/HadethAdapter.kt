package com.example.islamiapp.ui.home.tabs.hadeth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.islamiapp.databinding.ItemHadethBinding
import com.example.islamiapp.domain.model.Hadeth

class HadethAdapter(
    private val onItemClick: (Hadeth) -> Unit
) : ListAdapter<Hadeth, HadethAdapter.ViewHolder>(DiffCallback) {
    class ViewHolder(val binding: ItemHadethBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemHadethBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hadeth = getItem(position)
        holder.binding.title.text = hadeth.title
        holder.binding.root.setOnClickListener {
            onItemClick(hadeth)
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<Hadeth>() {
        override fun areItemsTheSame(oldItem: Hadeth, newItem: Hadeth): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Hadeth, newItem: Hadeth): Boolean =
            oldItem == newItem
    }
}
