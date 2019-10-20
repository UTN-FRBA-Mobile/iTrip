package com.android.itrip.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.R
import com.android.itrip.databinding.CitytovisitItemBinding
import com.android.itrip.fragments.TripFragmentDirections
import com.android.itrip.models.CiudadAVisitar
import com.squareup.picasso.Picasso


class TripAdapter(private val travels: List<CiudadAVisitar>) :
    ListAdapter<CiudadAVisitar, RecyclerView.ViewHolder>(TripDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: CitytovisitItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.citytovisit_item, parent, false
            )
        val viewHolder = TripHolder(binding)
        binding.lifecycleOwner = viewHolder
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val travel = getItem(position)
        (holder as TripHolder).bind(travel)
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItem(position: Int) = travels[position]

    override fun getItemCount() = travels.size

    class TripHolder(private val binding: CitytovisitItemBinding) :
        RecyclerView.ViewHolder(binding.root), LifecycleOwner {
        private val lifecycleRegistry = LifecycleRegistry(this)

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

        fun bind(ciudadAVisitar: CiudadAVisitar) {
            binding.apply {
                this.ciudadAVisitar = ciudadAVisitar
                setImage(ciudadAVisitar)
                travelDate.text =
                    "Desde " + ciudadAVisitar.inicio.time + ", Hasta " + ciudadAVisitar.fin.time
                viewActivitiesMaterialButton.setOnClickListener { view: View ->
                    val bundle = bundleOf(
                        "ciudadAVisitar" to ciudadAVisitar
                    )
                    view.findNavController().navigate(
                        TripFragmentDirections.actionTripFragmentToScheduleFragment().actionId,
                        bundle
                    )
                }
                modifyCityToTravelButton.setOnClickListener { view: View ->
                    val bundle = bundleOf(
                        "ciudadAVisitar" to ciudadAVisitar
                    )
                    view.findNavController().navigate(
                        TripFragmentDirections.actionTripFragmentToDestinationListFragment().actionId,
                        bundle
                    )
                }
                removeCityToTravelButton.setOnClickListener {
                    Toast.makeText(
                        it.context,
                        "Deberia ser removido el destino",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        private fun setImage(ciudadAVisitar: CiudadAVisitar) {
            ciudadAVisitar.detalle_ciudad?.imagen?.let {
                Picasso.get()
                    .load(ciudadAVisitar.detalle_ciudad.imagen)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .fit()
                    .into(binding.travelImg)
            }
        }
    }

}

private class TripDiffCallback : DiffUtil.ItemCallback<CiudadAVisitar>() {

    override fun areItemsTheSame(oldItem: CiudadAVisitar, newItem: CiudadAVisitar) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: CiudadAVisitar, newItem: CiudadAVisitar) =
        oldItem == newItem

}