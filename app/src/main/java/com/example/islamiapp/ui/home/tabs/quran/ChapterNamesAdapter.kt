package com.example.islamiapp.ui.home.tabs.quran

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.islamiapp.databinding.ItemChapterNameBinding
import com.example.islamiapp.domain.model.QuranChapter

class ChapterNamesAdapter(
    private val onItemClick: (QuranChapter) -> Unit
) : ListAdapter<QuranChapter, ChapterNamesAdapter.ViewHolder>(DiffCallback) {
    class ViewHolder(val binding: ItemChapterNameBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemChapterNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chapter = getItem(position)
        holder.binding.title.text = chapter.name
        holder.binding.verseCount.text = chapter.verseCount.toString()
        holder.binding.root.setOnClickListener {
            onItemClick(chapter)
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<QuranChapter>() {
        override fun areItemsTheSame(oldItem: QuranChapter, newItem: QuranChapter): Boolean =
            oldItem.number == newItem.number

        override fun areContentsTheSame(oldItem: QuranChapter, newItem: QuranChapter): Boolean =
            oldItem == newItem
    }
}
