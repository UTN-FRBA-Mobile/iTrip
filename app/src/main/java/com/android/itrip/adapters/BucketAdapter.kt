package com.android.itrip.adapters


import android.view.LayoutInflater
import android.view.View
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
import com.android.itrip.databinding.BucketItemBinding
import com.android.itrip.fragments.ScheduleFragmentDirections
import com.android.itrip.models.ActividadARealizar
import com.android.itrip.viewModels.ScheduleViewModel
import java.util.logging.Logger


class BucketAdapter(private val scheduleViewModel: ScheduleViewModel) :
    ListAdapter<ActividadARealizar, ActivitiesHolder>(
        ActividadARealizarDiffItemCallback()
    ) {

    private val logger = Logger.getLogger(this::class.java.name)

    init {
        scheduleViewModel.actividadesARealizar.observeForever {
            try {
                notifyItemRangeRemoved(0, itemCount)
                submitList(scheduleViewModel.actividadesARealizar.value)
            } catch (e: Exception) {
                logger.info(e.toString())
            }
        }
    }

    override fun onBindViewHolder(holder: ActivitiesHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    override fun getItem(position: Int): ActividadARealizar {
        return scheduleViewModel.actividadesARealizar.value?.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        return scheduleViewModel.actividadesARealizar.value?.get(position)?.id ?: 0
    }

    override fun getItemCount(): Int {
        return scheduleViewModel.actividadesARealizar.value?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivitiesHolder {
        val binding: BucketItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.bucket_item, parent, false
            )
        parent.invalidate()
        val viewHolder = ActivitiesHolder(binding)
        binding.lifecycleOwner = viewHolder
        return viewHolder
    }

}

class ActivitiesHolder(
    private val binding: BucketItemBinding
) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

    fun bind(item: ActividadARealizar, position: Int) {
        binding.apply {
            actividadARealizar = item
            bucketActivitydetailsButton.setOnClickListener {
                val bundle = bundleOf(
                    "actividad" to item.detalle_actividad
                )
                it.findNavController()
                    .navigate(
                        ScheduleFragmentDirections.actionScheduleFragmentToActivityDetailsFragment().actionId
                        , bundle
                    )
            }
            this.position = position
        }
    }
}

private class ActividadARealizarDiffItemCallback : DiffUtil.ItemCallback<ActividadARealizar>() {

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
