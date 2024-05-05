package com.example.booking.establishments.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.booking.R
import com.example.booking.common.utils.createDiffCallback
import com.example.booking.databinding.LayoutEstablishmentItemBinding
import com.example.booking.establishments.domain.model.Establishment

/**
 * [PagingDataAdapter] списка заведений
 */
class EstablishmentListAdapter(
    private val onItemClick: (establishment: Establishment) -> Unit = {}
) : PagingDataAdapter<Establishment, EstablishmentListAdapter.EstablishmentViewHolder>(COMPARATOR) {
    inner class EstablishmentViewHolder(
        private val binding: LayoutEstablishmentItemBinding
    ) :  RecyclerView.ViewHolder(binding.root) {
        fun bind(establishment: Establishment) {
            with(binding) {
                card.setOnClickListener {
                    onItemClick.invoke(establishment)
                }
                imageViewEstablishmentPreview.load(establishment.imageLink) {
                    crossfade(true)
                    placeholder(R.drawable.loading_img)
                }
                textViewTitle.text = establishment.title
                textViewDescription.text = establishment.description
                textViewAddress.text = establishment.address
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EstablishmentViewHolder =
        EstablishmentViewHolder(
            LayoutEstablishmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: EstablishmentViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!)
    }

    companion object {
        private val COMPARATOR = createDiffCallback<Establishment>(
            areItemsTheSame = { oldItem, newItem -> oldItem == newItem },
            areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
        )
    }
}