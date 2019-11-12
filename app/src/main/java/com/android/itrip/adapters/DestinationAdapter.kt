package com.android.itrip.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.R
import com.android.itrip.databinding.DestinationItemBinding
import com.android.itrip.models.Ciudad
import com.android.itrip.viewModels.DestinationViewModel
import com.squareup.picasso.Picasso

class DestinationAdapter(
    context: Context,
    private val destinationViewModel: DestinationViewModel,
    private val addCityCallback: (Ciudad) -> Unit,
    private val viewActivitiesforCityCallback: (Ciudad) -> Unit
) : RecyclerView.Adapter<DestinationAdapter.DestinationHolder>() {


    override fun onBindViewHolder(holder: DestinationHolder, position: Int) {
        holder.bind(
            getItem(position),
            buttonClickScaleAnimation,
            addCityCallback,
            viewActivitiesforCityCallback
        )
    }

    init {
        destinationViewModel.ciudades.observeForever { notifyDataSetChanged() }
    }

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    private fun getItem(position: Int) = destinationViewModel.ciudades.value!![position]

    override fun getItemCount() = destinationViewModel.ciudades.value?.size ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationHolder {
        val binding: DestinationItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.destination_item, parent, false
            )
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

        fun bind(
            item: Ciudad,
            buttonClickScaleAnimation: Animation,
            addCityCallback: (Ciudad) -> Unit,
            viewActivitiesforCityCallback: (Ciudad) -> Unit
        ) {
            binding.apply {
                setImage(item)
                textviewDestinationListName.text = item.nombre
                ciudad = item
                relativelayoutDestinationListAdd.setOnClickListener {
                    it.startAnimation(buttonClickScaleAnimation)
                    addCityCallback(item)
                }
                textviewDestinationListActividades.setOnClickListener {
                    it.startAnimation(buttonClickScaleAnimation)
                    viewActivitiesforCityCallback(item)
                }
            }
        }

        private fun setImage(ciudad: Ciudad) {
            ciudad.imagen.apply {
                Picasso.get()
                    .load(this)
                    .error(R.drawable.logo)
                    .fit()
                    .into(binding.imageviewDestinationListPicture)
            }
        }

    }

    private val buttonClickScaleAnimation =
        AnimationUtils.loadAnimation(context, R.anim.nav_default_pop_enter_anim)

}