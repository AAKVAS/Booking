package com.example.booking.common.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booking.databinding.LayoutBaseNavItemBinding


open class BaseNavAdapter : ListAdapter<NavListItem, BaseNavAdapter.ViewHolder>(COMPARATOR) {

    inner class ViewHolder(private val binding: LayoutBaseNavItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NavListItem) {
            with(binding) {
                textViewTitle.text = item.title
                textViewDescription.text = item.description
                if (item.imageSrc != null) {
                    imageViewIcon.setImageResource(item.imageSrc)
                    imageViewIcon.isVisible = true
                } else {
                    imageViewIcon.isVisible = false
                }
                navItemLayout.setOnClickListener {
                    item.onClick()
                }
                if (item.imageSrc != null) {
                    imageViewIcon.setImageResource(item.imageSrc)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
        : BaseNavAdapter.ViewHolder {
        return ViewHolder(
            LayoutBaseNavItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<NavListItem>() {
            override fun areItemsTheSame(oldItem: NavListItem, newItem: NavListItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: NavListItem, newItem: NavListItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}