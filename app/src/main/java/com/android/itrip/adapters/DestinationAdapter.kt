package com.android.itrip.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.R
import com.android.itrip.database.Destination
import com.android.itrip.databinding.DestinationItemBinding
import com.android.itrip.viewModels.DestinationViewModel
import com.squareup.picasso.Picasso

class DestinationAdapter(
    context: Context,
    private val destinationViewModel: DestinationViewModel,
    private val addCityCallback: (Destination) -> Unit,
    private val viewActivitiesforCityCallback: (Destination) -> Unit
) : ListAdapter<Destination, RecyclerView.ViewHolder>(DestinationDiffCallback()) {

    init {
        destinationViewModel.destinations.observeForever { notifyDataSetChanged() }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DestinationHolder).bind(getItem(position))
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    override fun getItem(position: Int) = destinationViewModel.destinations.value!![position]

    override fun getItemCount() = destinationViewModel.destinations.value?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: DestinationItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.destination_item, parent, false
            )
        with(binding) {
            relativelayoutDestinationListAdd.setOnClickListener { view ->
                view.startAnimation(buttonClickScaleAnimation)
                addCityCallback(binding.destination!!)
            }
            textviewDestinationListActividades.setOnClickListener { view ->
                view.startAnimation(buttonClickScaleAnimation)
                viewActivitiesforCityCallback(binding.destination!!)
            }
        }
        val viewHolder = DestinationHolder(binding)
        binding.lifecycleOwner = viewHolder
        return viewHolder
    }

    class DestinationHolder(
        private val binding: DestinationItemBinding
    ) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {
        private val lifecycleRegistry = LifecycleRegistry(this)

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

        fun bind(item: Destination) {
            binding.apply {
                setImage(item)
                textviewDestinationListName.text = item.name
                destination = item
            }
        }

        private fun setImage(destination: Destination) {
            destination.picture?.let {
                Picasso.get()
                    .load(it)
                    .error(R.drawable.logo)
                    .fit()
                    .into(binding.imageviewDestinationListPicture)
            }
        }

    }

    private val buttonClickScaleAnimation =
        AnimationUtils.loadAnimation(context, R.anim.nav_default_pop_enter_anim)

}

private class DestinationDiffCallback : DiffUtil.ItemCallback<Destination>() {

    override fun areItemsTheSame(oldItem: Destination, newItem: Destination): Boolean {
        return oldItem.destinationId == newItem.destinationId
    }

    override fun areContentsTheSame(oldItem: Destination, newItem: Destination): Boolean {
        return oldItem == newItem
    }

}