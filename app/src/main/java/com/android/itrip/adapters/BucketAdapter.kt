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
import com.android.itrip.databinding.BucketItemBinding
import com.android.itrip.models.ActividadARealizar


class BucketAdapter(private val actividadesARealizar: LiveData<List<ActividadARealizar>>) :
    ListAdapter<ActividadARealizar, RecyclerView.ViewHolder>(ActividadARealizarDiffCallback()) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val actividad = getItem(position)
        (holder as ActivitiesHolder).bind(actividad)
    }

    override fun getItem(position: Int): ActividadARealizar {
        return actividadesARealizar.value?.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return actividadesARealizar.value?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: BucketItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.bucket_item, parent, false
            )
        val viewHolder = ActivitiesHolder(binding)
        binding.lifecycleOwner = viewHolder
        return viewHolder
    }

    class ActivitiesHolder(
        private val binding: BucketItemBinding
    ) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {
        private val lifecycleRegistry = LifecycleRegistry(this)
        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

        fun bind(item: ActividadARealizar) {
            binding.apply {
                actividadARealizar = item
                actividad = item.detalle_actividad
                bucket1Textview.text = item.detalle_actividad.nombre
                position = 1
            }
        }
    }

}

private class ActividadARealizarDiffCallback : DiffUtil.ItemCallback<ActividadARealizar>() {

    override fun areItemsTheSame(
        oldItem: ActividadARealizar,
        newItem: ActividadARealizar
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ActividadARealizar,
        newItem: ActividadARealizar
    ): Boolean {
        return oldItem == newItem
    }
}