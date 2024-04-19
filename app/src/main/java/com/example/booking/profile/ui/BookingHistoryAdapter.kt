package com.example.booking.profile.ui

import android.app.AlertDialog
import android.content.Context
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.text.HtmlCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.booking.R
import com.example.booking.common.utils.createDiffCallback
import com.example.booking.common.utils.toTextTime
import com.example.booking.databinding.LayoutBookingDateItemBinding
import com.example.booking.databinding.LayoutBookingItemBinding
import com.example.booking.profile.domain.model.BookingStatuses
import com.example.booking.profile.ui.model.BookingHistoryUiItem

/**
 * [PagingDataAdapter] для списка истории бронирований
 */
class BookingHistoryAdapter(
    private val cancelBooking: (bookingId: Long) -> Unit = {},
    private val deleteBooking: (bookingId: Long) -> Unit = {},
    private val onItemClick: (establishmentId: Long) -> Unit = {}
) : PagingDataAdapter<BookingHistoryUiItem, RecyclerView.ViewHolder>(COMPARATOR) {
    /**
     * [ViewHolder] даты бронирования
     */
    inner class DateViewHolder(
        private  val binding: LayoutBookingDateItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(date: BookingHistoryUiItem.Date) {
            with(binding) {
                textViewDate.text = date.date
            }
        }
    }

    /**
     * [ViewHolder] бронирования
     */
    inner class BookingViewHolder(
        private val binding: LayoutBookingItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(booking: BookingHistoryUiItem.Booking) {
            with(binding) {
                textViewEstablishmentTitle.text = booking.establishment.title
                textViewStart.text = booking.startedAt.toTextTime()
                textViewEnd.text = booking.endedAt.toTextTime()
                textViewStatus.text = getStatus(itemView.context!!, booking.statusId)
                backgroundImg.load(booking.establishment.imageLink) {
                    crossfade(true)
                    placeholder(R.drawable.loading_img)
                }
                imgButtonMore.setOnClickListener {
                    showPopupMenu(imgButtonMore, booking)
                }
                card.setOnClickListener {
                    onItemClick(booking.establishment.id)
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

    private fun showPopupMenu(view: View, booking: BookingHistoryUiItem.Booking) {
        with(PopupMenu(view.context, view)) {
            when(booking.statusId) {
                BookingStatuses.BOOKED -> {
                    menu.add(view.context!!.getString(R.string.cancel_booking))
                    setOnMenuItemClickListener { _ ->
                        showConfirmCancelBookingDialog(view.context, booking.id)
                        return@setOnMenuItemClickListener true
                    }
                }
                else -> {
                    menu.add(view.context!!.getString(R.string.delete))
                    setOnMenuItemClickListener { _ ->
                        showConfirmDeleteBookingDialog(view.context, booking.id)
                        return@setOnMenuItemClickListener true
                    }
                }
            }
            show()
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

    private fun getStatus(context: Context, statusId: Int): Spanned {
        val prompt = context.getString(R.string.status)
        val status = when(statusId) {
            BookingStatuses.BOOKED -> context.getString(R.string.booked)
            BookingStatuses.ENDED -> context.getString(R.string.ended)
            BookingStatuses.CANCELED_BY_CLIENT -> context.getString(R.string.canceled_by_client)
            BookingStatuses.CANCELED_BY_PROVIDER -> context.getString(R.string.canceled_by_provider)
            else -> context.getString(R.string.unidentified_status)
        }
        val spannableString = SpannableString("$prompt $status")


        val promptSpan = TypefaceSpan("sans-serif-light")
        spannableString.setSpan(promptSpan, 0, prompt.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)


        val statusSpan = TypefaceSpan("sans-serif")
        spannableString.setSpan(statusSpan, prompt.length + 1, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannableString
    }

    private fun showConfirmDeleteBookingDialog(context: Context, bookingId: Long) {
        with(AlertDialog.Builder(context)) {
            setMessage(R.string.delete_booking_confirm)
            setPositiveButton(R.string.yes) { _, _ ->
                deleteBooking(bookingId)
            }
            setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    private fun showConfirmCancelBookingDialog(context: Context, bookingId: Long) {
        with(AlertDialog.Builder(context)) {
            setMessage(R.string.cancel_booking_confirm)
            setPositiveButton(R.string.yes) { _, _ ->
                cancelBooking(bookingId)
            }
            setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
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