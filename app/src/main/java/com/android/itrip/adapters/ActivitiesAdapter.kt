package com.android.itrip.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.R
import com.android.itrip.databinding.ActivitiesItemBinding
import com.android.itrip.fragments.ActivitiesListFragmentDirections
import com.android.itrip.models.Actividad
import com.android.itrip.models.MapDestination
import com.squareup.picasso.Picasso

class ActivitiesAdapter(actividades: LiveData<List<Actividad>>) :
    ListAdapter<Actividad, RecyclerView.ViewHolder>(ActivitiesDiffCallback()) {

    private var _actividades: LiveData<List<Actividad>> = actividades

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val actividad = getItem(position)
        (holder as ActivitiesHolder).bind(actividad)
    }

    override fun getItem(position: Int): Actividad {
        return _actividades.value!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        var size = 0
        _actividades.value?.let {
            size = _actividades.value!!.size
        }
        return size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: ActivitiesItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.activities_item, parent, false
            )
        binding.mapImageView.setOnClickListener { view: View ->
            val mapDestination = MapDestination(
                binding.actividadModel!!.nombre,
                binding.actividadModel!!.latitud,
                binding.actividadModel!!.longitud
            )
            val bundle = bundleOf(
                "mapDestination" to mapDestination
            )
            view.findNavController()
                .navigate(
                    ActivitiesListFragmentDirections.actionActivitiesListFragmentToMapsFragment().actionId
                    , bundle
                )
        }
        val viewHolder = ActivitiesHolder(binding)
        binding.lifecycleOwner = viewHolder
        return viewHolder
    }

    class ActivitiesHolder(
        private val binding: ActivitiesItemBinding
    ) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {
        private val lifecycleRegistry = LifecycleRegistry(this)
        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

        fun bind(item: Actividad) {
            binding.apply {
                actividadModel = item
                actividadNameTextView.text = item.nombre
                if (!item.imagen.isNullOrBlank()) {
                    Picasso.get()
                        .load(item.imagen)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .fit()
                        .into(activityImageView)
                }
            }
        }
    }

}

private class ActivitiesDiffCallback : DiffUtil.ItemCallback<Actividad>() {

    override fun areItemsTheSame(
        oldItem: Actividad,
        newItem: Actividad
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Actividad,
        newItem: Actividad
    ): Boolean {
        return oldItem == newItem
    }
}