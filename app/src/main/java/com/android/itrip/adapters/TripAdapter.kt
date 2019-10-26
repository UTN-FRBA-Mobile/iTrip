package com.android.itrip.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
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
import com.android.itrip.viewModels.TripViewModel
import com.squareup.picasso.Picasso
import java.util.logging.Logger


class TripAdapter(
    private val tripViewModel: TripViewModel,
    private val deleteCallback: (CiudadAVisitar) -> Unit,
    private val viewCallback: (CiudadAVisitar) -> Unit
) :
    ListAdapter<CiudadAVisitar, RecyclerView.ViewHolder>(TripDiffCallback()) {
    private val logger = Logger.getLogger("prueba")

    init {
        tripViewModel.ciudadesAVisitar.observeForever {
            logger.info("tripViewModel.ciudadesAVisitar.observeForever")
            notifyItemRangeRemoved(0, itemCount)
            notifyDataSetChanged()
            submitList(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: CitytovisitItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.citytovisit_item, parent, false
            )
        parent.invalidate()
        val viewHolder = TripHolder(binding, deleteCallback, viewCallback)
        binding.lifecycleOwner = viewHolder
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TripHolder).bind(getItem(position))
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItem(position: Int) = tripViewModel.ciudadesAVisitar.value!![position]

    override fun getItemCount() = tripViewModel.ciudadesAVisitar.value!!.size

    class TripHolder(
        private val binding: CitytovisitItemBinding,
        private val deleteCallback: (CiudadAVisitar) -> Unit,
        private val viewCallback: (CiudadAVisitar) -> Unit
    ) :
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
                viewActivitiesMaterialButton.setOnClickListener {
                    viewCallback(ciudadAVisitar)
                    val bundle = bundleOf(
                        "ciudadAVisitar" to ciudadAVisitar
                    )
                    it.findNavController().navigate(
                        TripFragmentDirections.actionTripFragmentToScheduleFragment().actionId,
                        bundle
                    )
                }
                removeCityToTravelButton.setOnClickListener {
                    deleteCallback(ciudadAVisitar)
                }
            }
        }

        private fun setImage(ciudadAVisitar: CiudadAVisitar) {
            ciudadAVisitar.detalle_ciudad?.imagen?.let {
                Picasso.get()
                    .load(it)
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