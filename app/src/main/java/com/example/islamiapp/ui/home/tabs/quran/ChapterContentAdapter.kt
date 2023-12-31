package com.example.islamiapp.ui.home.tabs.quran

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.islamiapp.databinding.SuraContentItemBinding

class ChapterContentAdapter(private val contents: List<String>) :
    RecyclerView.Adapter<ChapterContentAdapter.ViewHolder>() {
    class ViewHolder(val binding: SuraContentItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SuraContentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = contents.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val content: String = contents[position]
            binding.content.text = content
        }
    }
}