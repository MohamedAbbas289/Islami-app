package com.example.islamiapp.ui.home.tabs.quran

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.islamiapp.R
import com.example.islamiapp.databinding.ItemQuranSearchResultBinding
import com.example.islamiapp.domain.model.QuranSearchResult

class QuranSearchResultsAdapter(
    private val onResultClick: (QuranSearchResult) -> Unit
) : ListAdapter<QuranSearchResult, QuranSearchResultsAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemQuranSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onResultClick)
    }

    class ViewHolder(
        private val binding: ItemQuranSearchResultBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: QuranSearchResult, onResultClick: (QuranSearchResult) -> Unit) {
            binding.verseReference.text = binding.root.context.getString(
                R.string.quran_search_result_reference,
                result.chapter.name,
                result.verse.number
            )
            binding.verseText.text = result.verse.text
            binding.root.setOnClickListener { onResultClick(result) }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<QuranSearchResult>() {
        override fun areItemsTheSame(
            oldItem: QuranSearchResult,
            newItem: QuranSearchResult
        ): Boolean = oldItem.chapter.number == newItem.chapter.number &&
                oldItem.verse.number == newItem.verse.number

        override fun areContentsTheSame(
            oldItem: QuranSearchResult,
            newItem: QuranSearchResult
        ): Boolean = oldItem == newItem
    }
}
