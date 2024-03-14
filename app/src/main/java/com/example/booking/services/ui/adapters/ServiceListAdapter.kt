package com.example.booking.services.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.booking.R
import com.example.booking.common.utils.createDiffCallback
import com.example.booking.databinding.LayoutServiceItemBinding
import com.example.booking.services.domain.model.Service


class ServiceListAdapter(
    private val onItemClick: (service: Service) -> Unit = {}
) : PagingDataAdapter<Service, ServiceListAdapter.ServiceViewHolder>(COMPARATOR) {
    inner class ServiceViewHolder(
        private val binding: LayoutServiceItemBinding
    ) :  RecyclerView.ViewHolder(binding.root) {
        fun bind(service: Service) {
            with(binding) {
                card.setOnClickListener {
                    onItemClick.invoke(service)
                }
                imageViewServicePreview.load(service.imageLink) {
                    crossfade(true)
                    placeholder(R.drawable.loading_img)
                }
                textViewTitle.text = service.title
                textViewDescription.text = service.description
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
        private val COMPARATOR = createDiffCallback<Service>(
            areItemsTheSame = { oldItem, newItem -> oldItem == newItem },
            areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
        )
    }
}