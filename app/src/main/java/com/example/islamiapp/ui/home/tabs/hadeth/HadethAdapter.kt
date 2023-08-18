package com.example.islamiapp.ui.home.tabs.hadeth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.islamiapp.databinding.ItemHadethBinding
import com.example.islamiapp.model.Hadeth

class HadethAdapter(private var ahadeth: List<Hadeth>?) :
    RecyclerView.Adapter<HadethAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemHadethBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemHadethBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = ahadeth?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val hadeth: Hadeth = ahadeth!![position]
            binding.title.text = hadeth.title
            if (onItemClickListener != null) {
                binding.root.setOnClickListener {
                    onItemClickListener?.onItemClick(position, hadeth)
                }
            }
        }
    }

    fun bindItems(newList: List<Hadeth>) {
        ahadeth = newList
        notifyDataSetChanged()
    }

    var onItemClickListener: OnItemClickListener? = null

    fun interface OnItemClickListener {
        fun onItemClick(position: Int, hadeth: Hadeth)
    }
}