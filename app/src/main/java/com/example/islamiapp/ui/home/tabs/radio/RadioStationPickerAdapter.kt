package com.example.islamiapp.ui.home.tabs.radio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.islamiapp.databinding.ItemRadioStationBinding
import com.example.islamiapp.domain.model.RadioStation

class RadioStationPickerAdapter(
    private val onStationClick: (RadioStation) -> Unit
) : ListAdapter<RadioStation, RadioStationPickerAdapter.ViewHolder>(DiffCallback) {
    private var selectedStationId: Int? = null

    fun selectStation(stationId: Int?) {
        val previousId = selectedStationId
        selectedStationId = stationId
        currentList.indexOfFirst { it.id == previousId }
            .takeIf { it >= 0 }
            ?.let(::notifyItemChanged)
        currentList.indexOfFirst { it.id == stationId }
            .takeIf { it >= 0 }
            ?.let(::notifyItemChanged)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemRadioStationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            station = getItem(position),
            isSelected = getItem(position).id == selectedStationId,
            onStationClick = onStationClick
        )
    }

    class ViewHolder(
        private val binding: ItemRadioStationBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            station: RadioStation,
            isSelected: Boolean,
            onStationClick: (RadioStation) -> Unit
        ) = with(binding.stationOption) {
            text = station.name
            isChecked = isSelected
            setOnClickListener { onStationClick(station) }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<RadioStation>() {
        override fun areItemsTheSame(oldItem: RadioStation, newItem: RadioStation): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: RadioStation, newItem: RadioStation): Boolean =
            oldItem == newItem
    }
}
