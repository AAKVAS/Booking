package com.example.booking.profile.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.booking.R
import com.example.booking.common.utils.createDiffCallback
import com.example.booking.common.utils.toTextTime
import com.example.booking.databinding.LayoutBookingDateItemBinding
import com.example.booking.databinding.LayoutBookingItemBinding
import com.example.booking.profile.ui.model.BookingHistoryUiItem


class BookingHistoryAdapter
    : PagingDataAdapter<BookingHistoryUiItem, RecyclerView.ViewHolder>(COMPARATOR)
{
    inner class DateViewHolder(
        private  val binding: LayoutBookingDateItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(date: BookingHistoryUiItem.Date) {
            with(binding) {
                textViewDate.text = date.date
            }
        }
    }

    inner class BookingViewHolder(
        private val binding: LayoutBookingItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(booking: BookingHistoryUiItem.Booking) {
            with(binding) {
                textViewServiceTitle.text = booking.service.title
                textViewStart.text = booking.startedAt.toTextTime()
                textViewEnd.text = booking.endedAt.toTextTime()
                textViewStatus.text = booking.status
                backgroundImg.load(booking.service.imageLink) {
                    crossfade(true)
                    placeholder(R.drawable.loading_img)
                }
                imgButtonMore.setOnClickListener {
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is BookingHistoryUiItem.Booking -> TYPE_BOOKING
            is BookingHistoryUiItem.Date -> TYPE_DATE
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_BOOKING ->
                BookingViewHolder(
                    LayoutBookingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            TYPE_DATE ->
                DateViewHolder(
                    LayoutBookingDateItemBinding.inflate(LayoutInflater.from(parent.context), parent,false)
                )
            else -> throw IllegalArgumentException("Invalid item type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BookingHistoryAdapter.BookingViewHolder -> {
                val item = (getItem(position) as BookingHistoryUiItem.Booking)
                holder.bind(item)
            }

            is BookingHistoryAdapter.DateViewHolder -> {
                val item = (getItem(position) as BookingHistoryUiItem.Date)
                holder.bind(item)
            }
        }
    }

    companion object {
        private val COMPARATOR = createDiffCallback<BookingHistoryUiItem>(
            areItemsTheSame = { oldItem, newItem -> oldItem == newItem },
            areContentsTheSame = { oldItem, newItem -> oldItem == newItem }
        )
        private const val TYPE_DATE = 0
        private const val TYPE_BOOKING = 1
    }
}