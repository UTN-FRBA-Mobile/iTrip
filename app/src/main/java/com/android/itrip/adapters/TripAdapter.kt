package com.android.itrip.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.R
import com.android.itrip.databinding.CitytovisitItemBinding
import com.android.itrip.models.CiudadAVisitar
import com.squareup.picasso.Picasso


class TripAdapter(
    _ciudadesAVisitar: List<CiudadAVisitar>?,
    private val viewCallback: (CiudadAVisitar) -> Unit
) : RecyclerView.Adapter<TripAdapter.TripHolder>(){

    private var cities: MutableList<CiudadAVisitar> = _ciudadesAVisitar?.toMutableList() ?: mutableListOf()

    override fun onBindViewHolder(holder: TripHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripHolder {
        val binding: CitytovisitItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.citytovisit_item, parent, false
            )
        val viewHolder = TripHolder(binding, viewCallback)
        binding.lifecycleOwner = viewHolder
        parent.invalidate()
        return viewHolder
    }

    override fun getItemId(position: Int) = position.toLong()

    fun getItem(position: Int) = cities[position]

    fun isEmpty() = cities.isEmpty()

    override fun getItemCount() = cities.size

    fun removeItem(ciudadAVisitar: CiudadAVisitar) {
        val position = cities.indexOf(ciudadAVisitar)
        cities.remove(ciudadAVisitar)
        notifyItemRemoved(position)
    }

    class TripHolder(
        private val binding: CitytovisitItemBinding,
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
                cityToVisitCardView.setOnClickListener {
                    viewCallback(ciudadAVisitar)
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