package com.example.booking.services.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.booking.R
import com.example.booking.databinding.LayoutServiceItemBinding
import com.example.booking.services.domain.model.Service


class ServiceListAdapter(
    private val onItemClick: (service: Service) -> Unit = {},
    private val onStarClick: (service: Service) -> Unit = {}
) : PagingDataAdapter<Service, ServiceListAdapter.ServiceViewHolder>(COMPARATOR) {
    inner class ServiceViewHolder(
        private val binding: LayoutServiceItemBinding
    ) :  RecyclerView.ViewHolder(binding.root) {
        fun bind(service: Service) {
            with(binding) {
                card.setOnClickListener {
                    onItemClick.invoke(service)
                }
                //ivServiceIcon.src = service.imageLink
                tvTitle.text = service.title
                tvDescription.text = service.description
                if (service.favorite) {
                    star.setBackgroundResource(R.drawable.filled_star)
                } else {
                    star.setBackgroundResource(R.drawable.star)
                }
                star.setOnClickListener {
                    onStarClick(service)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder =
        ServiceViewHolder(
            LayoutServiceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!)
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Service>() {
            override fun areItemsTheSame(oldItem: Service, newItem: Service): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Service, newItem: Service): Boolean {
                return oldItem == newItem
            }
        }
    }
}