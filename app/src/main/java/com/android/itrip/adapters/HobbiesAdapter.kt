package com.android.itrip.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.R
import com.android.itrip.databinding.HobbyItemBinding
import com.android.itrip.models.Answer

class HobbiesAdapter(private val hobbies: List<Answer>) :
    ListAdapter<Answer, RecyclerView.ViewHolder>(HobbyDiffCallback()) {

    val checkedHobbies = mutableListOf<Answer>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HobbyHolder).bind(getItem(position))
    }

    override fun getItem(position: Int) = hobbies[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getItemViewType(position: Int) = position

    override fun getItemCount() = hobbies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding: HobbyItemBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.hobby_item, parent, false
            )
        binding.apply {
            textviewQuizHobbiesHobby.setOnClickListener {
                answer?.let {
                    if (!checkedHobbies.contains(it)) {
                        textviewQuizHobbiesHobby.setBackgroundColor(Color.LTGRAY)
                        checkedHobbies.add(it)
                    } else {
                        textviewQuizHobbiesHobby.setBackgroundColor(Color.WHITE)
                        checkedHobbies.remove(it)
                    }
                }
            }
        }
        val viewHolder = HobbyHolder(binding)
        binding.lifecycleOwner = viewHolder
        return viewHolder
    }

    class HobbyHolder(
        private val binding: HobbyItemBinding
    ) : RecyclerView.ViewHolder(binding.root), LifecycleOwner {

        private val lifecycleRegistry = LifecycleRegistry(this)

        override fun getLifecycle() = lifecycleRegistry

        fun bind(item: Answer) {
            binding.apply { answer = item }
        }
    }

}

private class HobbyDiffCallback : DiffUtil.ItemCallback<Answer>() {

    override fun areItemsTheSame(oldItem: Answer, newItem: Answer) = oldItem.key == newItem.key

    override fun areContentsTheSame(oldItem: Answer, newItem: Answer) = oldItem == newItem

}