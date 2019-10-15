package com.android.itrip.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.R
import com.android.itrip.databinding.HobbieItemBinding
import com.android.itrip.models.Hobbie

class QuizAdapter(hobbies: List<Hobbie>) :
    ListAdapter<Hobbie, RecyclerView.ViewHolder>(HobbieDiffCallback()) {

    private val _hobbies: List<Hobbie> = hobbies

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HobbieHolder).bind(getItem(position))
    }

    override fun getItem(position: Int): Hobbie {
        return _hobbies[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return _hobbies.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: HobbieItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.hobbie_item, parent, false
            )
        binding.apply {
            hobbieTextview.setOnClickListener {
                hobbie?.let {
                    val tempHobbie = it
                    it.choosed = !tempHobbie.choosed
                    if (tempHobbie.choosed) hobbieTextview.setBackgroundColor(Color.LTGRAY)
                    else hobbieTextview.setBackgroundResource(R.drawable.bottom_line_border)
                }
            }
        }
        val viewHolder = HobbieHolder(binding)
        binding.lifecycleOwner = viewHolder
        return viewHolder
    }

    class HobbieHolder(
        private val binding: HobbieItemBinding
    ) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {
        private val lifecycleRegistry = LifecycleRegistry(this)
        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

        fun bind(item: Hobbie) {
            binding.apply {
                hobbie = item
            }
        }
    }

}

private class HobbieDiffCallback : DiffUtil.ItemCallback<Hobbie>() {

    override fun areItemsTheSame(
        oldItem: Hobbie,
        newItem: Hobbie
    ): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(
        oldItem: Hobbie,
        newItem: Hobbie
    ): Boolean {
        return oldItem == newItem
    }
}