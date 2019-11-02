package com.android.itrip.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.R
import com.android.itrip.database.Destination
import com.android.itrip.databinding.DestinationItemBinding
import com.android.itrip.viewModels.DestinationViewModel

class DestinationAdapter(
    private val destinationViewModel: DestinationViewModel,
    private val addCityCallback: (Destination) -> Unit,
    private val viewActivitiesforCityCallback: (Destination) -> Unit
) :
    ListAdapter<Destination, RecyclerView.ViewHolder>(DestinationDiffCallback()) {

    init {
        destinationViewModel.destinations.observeForever { notifyDataSetChanged() }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DestinationHolder).bind(getItem(position))
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItem(position: Int): Destination {
        return destinationViewModel.destinations.value!![position]
    }

    override fun getItemCount(): Int {
        var size = 0
        destinationViewModel.destinations.value?.let {
            size = destinationViewModel.destinations.value!!.size
        }
        return size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: DestinationItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.destination_item, parent, false
            )
        binding.addImagebutton.setOnClickListener {
            addCityCallback(binding.destination!!)
        }
        binding.activitiesButton.setOnClickListener {
            viewActivitiesforCityCallback(binding.destination!!)
        }
        val viewHolder = DestinationHolder(binding)
        binding.lifecycleOwner = viewHolder
        return viewHolder
    }

    class DestinationHolder(
        private val binding: DestinationItemBinding
    ) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {
        private val lifecycleRegistry = LifecycleRegistry(this)

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

        fun bind(item: Destination) {
            binding.apply {
                destinationNameTextView.text = item.name
                destination = item
            }
        }
    }

}

private class DestinationDiffCallback : DiffUtil.ItemCallback<Destination>() {

    override fun areItemsTheSame(
        oldItem: Destination,
        newItem: Destination
    ): Boolean {
        return oldItem.destinationId == newItem.destinationId
    }

    override fun areContentsTheSame(
        oldItem: Destination,
        newItem: Destination
    ): Boolean {
        return oldItem == newItem
    }
}