package com.android.itrip.adapters


import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.R
import com.android.itrip.databinding.TravelItemBinding
import com.android.itrip.fragments.HomeFragmentDirections
import com.android.itrip.models.Viaje
import com.squareup.picasso.Picasso


class TravelAdapter(private val deleteCallback: (Viaje) -> Unit) :
    ListAdapter<Viaje, RecyclerView.ViewHolder>(TravelDiffCallback()) {

    private var travels = mutableListOf<Viaje>()

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
        (holder as TravelHolder).bind(getItem(position))
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItem(position: Int) = travels[position]

    override fun getItemCount() = travels.size

    fun replaceItems(_travels: List<Viaje>) {
        travels = _travels.toMutableList()
        notifyDataSetChanged()
    }

    fun remove(adapterPosition: Int) {
        deleteCallback(getItem(adapterPosition))
    }

    fun deleteItem(travel: Viaje) {
        travels.remove(travel)
        notifyDataSetChanged()
    }

    fun hasTravels() = travels.size != 0

    class TravelHolder(
        private val binding: TravelItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root), LifecycleOwner {
        private val lifecycleRegistry = LifecycleRegistry(this)

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

        fun bind(viaje: Viaje) {
            binding.apply {
                travelItem = viaje
                travelItemCardView.setOnClickListener {
                    it.findNavController()
                        .navigate(
                            HomeFragmentDirections.actionHomeFragmentToTripFragment().actionId,
                            bundleOf(
                                "viajeID" to viaje.id
                            )
                        )
                }
                setImage(viaje)
            }
        }

        private fun setImage(viaje: Viaje) {
            if (viaje.ciudades_a_visitar.isEmpty()) {
                binding.imageGalleryConstraintLayout.children.forEach { it.visibility = GONE }
                (binding.imageGalleryConstraintLayout.children.first() as ImageView).visibility =
                    VISIBLE
            } else {
                val imageGalleryCount = binding.imageGalleryConstraintLayout.childCount
                var imageView: ImageView
                for (i in 0 until imageGalleryCount) {
                    imageView = binding.imageGalleryConstraintLayout.getChildAt(i) as ImageView
                    try {
                        viaje.ciudades_a_visitar[i].detalle_ciudad?.imagen?.let {
                            Picasso.get()
                                .load(it)
                                .fit()
                                .centerCrop()
                                .into(imageView)
                        }
                    } catch (e: Exception) {
                        imageView.visibility = GONE
                    }
                }
            }
        }
    }

}

private class TravelDiffCallback : DiffUtil.ItemCallback<Viaje>() {

    override fun areItemsTheSame(oldItem: Viaje, newItem: Viaje) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Viaje, newItem: Viaje) = oldItem == newItem

}