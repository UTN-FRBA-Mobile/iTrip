package com.android.itrip.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.R
import com.android.itrip.databinding.BucketEmptyItemBinding
import com.android.itrip.databinding.BucketItemBinding
import com.android.itrip.models.ActividadARealizar
import com.android.itrip.util.ActivityType
import com.android.itrip.viewModels.ScheduleViewModel
import com.squareup.picasso.Picasso

class BucketAdapter(
    private val scheduleViewModel: ScheduleViewModel,
    private val addActivityToBucketCallback: (ActividadARealizar) -> Unit,
    private val showActivityDetailsCallback: (ActividadARealizar) -> Unit
) :
    RecyclerView.Adapter<BucketAdapter.ActivitiesHolder>() {

    private var actividadARealizar = scheduleViewModel.actividadesARealizar.value ?: emptyList()

    init {
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

    private fun replaceItems(_activities: List<ActividadARealizar>) {
        actividadARealizar = _activities
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ActivitiesHolder, position: Int) {
        holder.bind(
            getItem(position),
            position,
            addActivityToBucketCallback,
            showActivityDetailsCallback
        )
    }

    private fun getItem(position: Int): ActividadARealizar {
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

        fun bind(
            item: ActividadARealizar,
            position: Int,
            addActivityToBucketCallback: (ActividadARealizar) -> Unit,
            showActivityDetailsCallback: (ActividadARealizar) -> Unit
        ) {
            when (itemViewType) {
                ActivityType.ACTIVITY -> bindActivity(item, position, showActivityDetailsCallback)
                ActivityType.EMPTY -> bindingEmpty(item, addActivityToBucketCallback)
            }
        }

        private fun bindActivity(
            item: ActividadARealizar,
            position: Int,
            showActivityDetailsCallback: (ActividadARealizar) -> Unit
        ) {
            (binding as BucketItemBinding).apply {
                //                if (position % 2 == 0)
//                    bucketItemConstraintLayout.setBackgroundColor(Color.LTGRAY)
//                else
//                    bucketItemConstraintLayout.setBackgroundColor(Color.WHITE)
                actividadARealizar = item
                var height =
                    bucketItemConstraintLayout.context.resources.displayMetrics.heightPixels / 8 * item.detalle_actividad.duracion
                if (item.detalle_actividad.duracion>1) height += 10 * item.detalle_actividad.duracion
//                bucketItemConstraintLayout.layoutParams.height = height
//                bucketItemConstraintLayout.requestLayout()
                bucketItemImageView.layoutParams.height = height
                bucketItemImageView.requestLayout()
                item.detalle_actividad.imagen?.let {
                    Picasso.get()
                        .load(it)
                        .resize(bucketItemConstraintLayout.width, height)
                        .onlyScaleDown()
                        .centerCrop()
                        .into(bucketItemImageView)
                }
                bucketItemConstraintLayout.setOnClickListener { showActivityDetailsCallback(item) }
            }
        }

        private fun bindingEmpty(
            item: ActividadARealizar,
            addActivityToBucketCallback: (ActividadARealizar) -> Unit
        ) {
            (binding as BucketEmptyItemBinding).bucketEmptyItemImageButton.apply {
                layoutParams.height =
                    context.resources.displayMetrics.heightPixels / 8 * item.detalle_actividad.duracion
                requestLayout()
                setOnClickListener {
                    addActivityToBucketCallback(item)
                }
            }
        }
    }

}
