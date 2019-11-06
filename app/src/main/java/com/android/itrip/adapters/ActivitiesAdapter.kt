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
import com.android.itrip.databinding.ActivitiesItemBinding
import com.android.itrip.models.Actividad
import com.squareup.picasso.Picasso


class ActivitiesAdapter(
    private val _actividades: LiveData<List<Actividad>>,
    private val activityDetailsCallback: (Actividad) -> Unit
) :
    ListAdapter<Actividad, RecyclerView.ViewHolder>(ActivitiesDiffCallback()) {

    init {
        _actividades.observeForever {
            notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ActivitiesHolder).bind(getItem(position), activityDetailsCallback)
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

        fun bind(
            item: Actividad,
            activityDetailsCallback: (Actividad) -> Unit
        ) {
            binding.apply {
                actividadModel = item
                activityConstraintLayout.setOnClickListener { activityDetailsCallback(item) }
                actividadNameTextView.text = item.nombre
                item.imagen?.let {
                    Picasso.get()
                        .load(it)
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