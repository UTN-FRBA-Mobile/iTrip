package com.android.itrip.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.R
import com.android.itrip.database.Destination
import com.android.itrip.databinding.DestinationItemBinding

class DestinationAdapter(destinations: LiveData<List<Destination>>) :
    ListAdapter<Destination, RecyclerView.ViewHolder>(DestinationDiffCallback()) {

    private var _destinations: LiveData<List<Destination>> = destinations


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val destination = getItem(position)
        (holder as DestinationHolder).bind(destination)
    }

    override fun getItem(position: Int): Destination {
        return _destinations.value!![position]
    }

    override fun getItemCount(): Int {
        var size = 0
        _destinations.value?.let {
            size = _destinations.value!!.size
        }
        return size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: DestinationItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.destination_item, parent, false
            )
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
                travelName.text = item.name
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