package com.example.booking.services.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booking.common.utils.createDiffCallback
import com.example.booking.databinding.LayoutHallItemBinding
import com.example.booking.services.domain.model.Hall

class HallsRecyclerAdapter(
    private val onItemClick: (hall: Hall) -> Unit
) : ListAdapter<Hall, HallsRecyclerAdapter.ViewHolder>(COMPARATOR) {

    inner class ViewHolder(private val binding: LayoutHallItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(hall: Hall) {
            with(binding) {
                textViewHallTitle.text = hall.title
                placesView.places = hall.places
                hallCardLayout.setOnClickListener {
                    onItemClick.invoke(hall)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutHallItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!)
    }

    companion object {
        private val COMPARATOR = createDiffCallback<Hall>(
            areItemsTheSame = { oldItem, newItem -> oldItem == newItem },
            areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
        )
    }
}