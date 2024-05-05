package com.example.booking.common.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.booking.databinding.LayoutSwitchItemBinding


open class SwitchAdapter : ListAdapter<SwitchListItem, SwitchAdapter.ViewHolder>(COMPARATOR) {

    inner class ViewHolder(private val binding: LayoutSwitchItemBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SwitchListItem) {
            with(binding) {
                textViewTitle.text = item.title
                switchView.isChecked = item.checked
                switchView.setOnCheckedChangeListener { buttonView, value ->
                    item.onCheckedChange(buttonView, value)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutSwitchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<SwitchListItem>() {
            override fun areItemsTheSame(oldItem: SwitchListItem, newItem: SwitchListItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: SwitchListItem, newItem: SwitchListItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}