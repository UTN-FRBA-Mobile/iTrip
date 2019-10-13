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

import com.android.itrip.databinding.TravelItemBinding
import com.android.itrip.models.Viaje
import com.squareup.picasso.Picasso


class TravelAdapter :
    ListAdapter<Viaje, RecyclerView.ViewHolder>(TravelDiffCallback()) {

    private var travels = emptyList<Viaje>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: TravelItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.travel_item, parent, false
            )
        val viewHolder = TravelHolder(binding)
        binding.lifecycleOwner = viewHolder
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val travel = getItem(position)
        (holder as TravelHolder).bind(travel)
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItem(position: Int) = travels[position]

    override fun getItemCount() = travels.size

    fun replaceItems(_travels: List<Viaje>) {
        travels = _travels
        notifyDataSetChanged()
    }

    class TravelHolder(private val binding: TravelItemBinding) :
        RecyclerView.ViewHolder(binding.root), LifecycleOwner {
        private val lifecycleRegistry = LifecycleRegistry(this)

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

        fun bind(viaje: Viaje) {
            binding.apply {
                setImage(viaje)
                destinationName.text = viaje.nombre
                travelDate.text =
                    super.itemView.context.getString(R.string.travels_date, viaje.inicio, viaje.fin)
            }
        }

        private fun setImage(viaje: Viaje) {
            viaje.imagen?.let {
                Picasso.get()
                    .load(R.drawable.logo)
                    .into(binding.travelImg)
            } ?: run {
                Picasso.get()
                    .load(viaje.imagen)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .resize(300, 300)
                    .centerCrop()
                    .into(binding.travelImg)
            }
        }
    }

}

private class TravelDiffCallback : DiffUtil.ItemCallback<Viaje>() {

    override fun areItemsTheSame(oldItem: Viaje, newItem: Viaje) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Viaje, newItem: Viaje) = oldItem == newItem

}