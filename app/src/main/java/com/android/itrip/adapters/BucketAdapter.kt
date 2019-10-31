package com.android.itrip.adapters


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.R
import com.android.itrip.database.Destination
import com.android.itrip.databinding.BucketEmptyItemBinding
import com.android.itrip.databinding.BucketItemBinding
import com.android.itrip.fragments.ScheduleFragmentDirections
import com.android.itrip.models.ActividadARealizar
import com.android.itrip.viewModels.ScheduleViewModel


interface ActivityType {
    companion object {
        const val EMPTY = 0
        const val ACTIVITY = 1
    }
}

class BucketAdapter(
    private val scheduleViewModel: ScheduleViewModel
) :
    RecyclerView.Adapter<BucketAdapter.ActivitiesHolder>() {

    private var actividadARealizar: List<ActividadARealizar>

    init {
        actividadARealizar = scheduleViewModel.actividadesARealizar.value ?: emptyList()
        scheduleViewModel.actividadesARealizar.observeForever {
            replaceItems(it)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).id) {
            0L -> ActivityType.EMPTY
            else -> ActivityType.ACTIVITY
        }
    }

    fun replaceItems(_activities: List<ActividadARealizar>) {
        actividadARealizar = _activities
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ActivitiesHolder, position: Int) {
        holder.bind(getItem(position), position, scheduleViewModel)
    }

    fun getItem(position: Int): ActividadARealizar {
        return actividadARealizar[position]
    }

    override fun getItemId(position: Int): Long {
        return actividadARealizar[position].id
    }

    override fun getItemCount(): Int {
        return actividadARealizar.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivitiesHolder {
        lateinit var binding: ViewDataBinding
        when (viewType) {
            ActivityType.ACTIVITY -> {
                binding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.bucket_item,
                        parent,
                        false
                    )
            }
            else -> {
                binding =
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.bucket_empty_item,
                        parent,
                        false
                    )
            }
        }
        parent.invalidate()
        return ActivitiesHolder(binding)
    }

    fun remove(position: Int) {
        scheduleViewModel.deleteToDoActivity(getItem(position))
    }

    class ActivitiesHolder(
        private val binding: ViewDataBinding
    ) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {
        private val lifecycleRegistry = LifecycleRegistry(this)

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

        fun bind(item: ActividadARealizar, position: Int, viewModel: ScheduleViewModel) {
            when (itemViewType) {
                ActivityType.ACTIVITY -> bindActivity(item, position)
                ActivityType.EMPTY -> bindingEmpty(item, viewModel)
            }
        }

        fun bindActivity(
            item: ActividadARealizar,
            position: Int
        ) {
            (binding as BucketItemBinding).apply {
                if (position % 2 == 0)
                    bucketItemConstraintLayout.setBackgroundColor(Color.LTGRAY)
                else
                    bucketItemConstraintLayout.setBackgroundColor(Color.WHITE)
                actividadARealizar = item
                bucketItemConstraintLayout.layoutParams.height =
                    bucketItemConstraintLayout.context.resources.displayMetrics.heightPixels / 9 * item.detalle_actividad!!.duracion
                bucketItemConstraintLayout.requestLayout()
                bucketItemConstraintLayout.setOnClickListener {
                    Toast.makeText(
                        it.context,
                        "Desliza izquierda para ver detalles. \nDesliza derecha para remover.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        fun bindingEmpty(item: ActividadARealizar, viewModel: ScheduleViewModel) {
            (binding as BucketEmptyItemBinding).apply {
                bucketEmptyItemConstraintLayout.layoutParams.height =
                    bucketEmptyItemConstraintLayout.context.resources.displayMetrics.heightPixels / 9 * item.detalle_actividad!!.duracion
                bucketEmptyItemConstraintLayout.requestLayout()
                bucketAddButton.setOnClickListener {
                    val destination = Destination(
                        viewModel.ciudadAVisitar.detalle_ciudad!!.id,
                        viewModel.ciudadAVisitar.detalle_ciudad!!.nombre
                    )
                    it.findNavController().navigate(
                        ScheduleFragmentDirections.actionScheduleFragmentToActivitiesListFragment().actionId,
                        bundleOf("destination" to destination)
                    )
                }
            }
        }

    }
}