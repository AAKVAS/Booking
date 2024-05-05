package com.example.booking.bookings.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.booking.MainActivity
import com.example.booking.R
import com.example.booking.bookings.domain.model.Booking
import com.example.booking.bookings.ui.model.BookingValidationStatus
import com.example.booking.bookings.ui.viewModel.BookingViewModel
import com.example.booking.common.ui.adapters.BaseNavAdapter
import com.example.booking.common.ui.adapters.NavListItem
import com.example.booking.common.utils.convertDate
import com.example.booking.common.utils.millisToDate
import com.example.booking.common.utils.timeToInt
import com.example.booking.common.utils.toTextTime
import com.example.booking.common.utils.toTime
import com.example.booking.databinding.FragmentBookingBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Фрагмент экрана бронирования
 */
@AndroidEntryPoint
class BookingFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: BookingViewModel.BookingViewModelFactory

    private lateinit var binding: FragmentBookingBinding

    private val args: BookingFragmentArgs by navArgs()
    private val viewModel: BookingViewModel by viewModels {
        BookingViewModel.provideFactory(viewModelFactory, args.bookingEstablishmentParam)
    }

    private val dateTimeBookingAdapter: BaseNavAdapter = BaseNavAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind()
        subscribeToViewModel()
    }

    private fun bind() {
        with(binding) {
            textViewEstablishmentTitle.text = args.bookingEstablishmentParam.title
            textViewHallTitle.text = args.bookingEstablishmentParam.hall.title
            placesView.places = args.bookingEstablishmentParam.hall.places
            placesView.setOnPlaceChanged {
                viewModel.updateBookingState(viewModel.bookingStateFlow.value.copy(place = it))
            }
            buttonBook.setOnClickListener {
                val status = viewModel.validateBooking()
                if (status != BookingValidationStatus.VALID) {
                    showNotValidMessage(status)
                } else {
                    viewModel.bookPlace()
                }
            }
            dateTimeRecycler.adapter = dateTimeBookingAdapter
            dateTimeRecycler.addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
        }
    }

    private fun subscribeToViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch { viewModel.bookingStateFlow.collect(::onBookingChanged)}
                launch { viewModel.bookingResultFlow.collect(::onBookingResult) }
                launch { viewModel.bookButtonEnabledStateFlow.collect(::onBookButtonEnabledChange) }
            }
        }
    }

    private fun onBookingChanged(booking: Booking) {
        dateTimeBookingAdapter.submitList(
            fillDateTimeBookingList(booking)
        )
        binding.placesView.places = booking.hall.places
    }

    private fun onBookButtonEnabledChange(enabled: Boolean) {
        binding.buttonBook.isVisible = enabled
    }

    private fun fillDateTimeBookingList(
        booking: Booking
    ): List<NavListItem> {
        val bookingDate = convertDate(
            dateString = booking.date.millisToDate(),
            originFormat = "dd.MM.yyyy"
        )
        val startedAt = booking.startedAt.toTextTime()
        val endedAt = booking.endedAt.toTextTime()

        val dateListItem = NavListItem(
            title = getString(R.string.choose_date),
            description = bookingDate,
            imageSrc = R.drawable.next
        ) {
            val booking = viewModel.bookingStateFlow.value
            showCalendar(booking.date) {
                viewModel.updateBookingState(booking.copy(date = it))
                viewModel.updatePlaces()
            }
        }

        val startedAtItem = NavListItem(
            title = getString(R.string.start),
            description = startedAt,
            imageSrc = R.drawable.next
        ) {
            val booking = viewModel.bookingStateFlow.value
            booking.startedAt.toTime { hours, minutes ->
                showTimePicker(
                    title = getString(R.string.booking_start),
                    onPosClick = { newHour, newMinute ->
                        val time = timeToInt(newHour, newMinute)
                        viewModel.updateBookingState(booking.copy(startedAt = time))
                        viewModel.updatePlaces()
                    },
                    hours = hours,
                    minutes = minutes
                )
            }
        }

        val endedAtItem = NavListItem(
            title = getString(R.string.end),
            description = endedAt,
            imageSrc = R.drawable.next
        ) {
            val booking = viewModel.bookingStateFlow.value
            booking.endedAt.toTime { hours, minutes ->
                showTimePicker(
                    title = getString(R.string.booking_end),
                    onPosClick = { newHour, newMinute ->
                        val time = timeToInt(newHour, newMinute)
                        viewModel.updateBookingState(booking.copy(endedAt = time))
                        viewModel.updatePlaces()
                    },
                    hours = hours,
                    minutes = minutes
                )
            }
        }

        return listOf(dateListItem, startedAtItem, endedAtItem)
    }


    private fun onBookingResult(result: Result<Unit>) {
        if (result.isSuccess) {
            showBookingSuccessMessage()
        } else {
            showBookingFailureMessage()
        }
    }

    private fun showTimePicker(
        title: String,
        onPosClick: (hours: Int, minutes: Int) -> Unit,
        hours: Int,
        minutes: Int
    ) {
        val picker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(hours)
                .setMinute(minutes)
                .setTitleText(title)
                .build()

        picker.addOnPositiveButtonClickListener {
            onPosClick(picker.hour, picker.minute)
        }

        picker.show(childFragmentManager, TIME_PICKER_TAG)
    }

    private fun showCalendar(
        date: Long = MaterialDatePicker.todayInUtcMilliseconds(),
        onPosClick: (selection: Long) -> Unit
    ) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(getString(R.string.choose_date))
            .setSelection(date)
            .build()

        datePicker.addOnPositiveButtonClickListener {
            onPosClick(datePicker.selection ?: MaterialDatePicker.todayInUtcMilliseconds())
        }

        datePicker.show(childFragmentManager, DATE_PICKER_TAG)
    }

    private fun showNotValidMessage(status: BookingValidationStatus) {
        val message = when(status) {
            BookingValidationStatus.PLACE_NOT_SELECTED -> R.string.choose_free_place
            BookingValidationStatus.WRONG_TIME -> R.string.end_must_be_more_than_start
            else -> -1
        }

        with(AlertDialog.Builder(context)) {
            setMessage(message)
            setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    private fun showBookingSuccessMessage() {
        with(AlertDialog.Builder(context)) {
            setMessage(R.string.success_booked)
            setPositiveButton(R.string.ok) { dialog, _ ->
                navToCatalog()
            }
            create()
            show()
        }
    }

    private fun navToCatalog() {
        (requireActivity() as MainActivity).navController
            .navigate(R.id.action_bookingFragment_to_catalogFragment)
    }

    private fun showBookingFailureMessage() {
        with(AlertDialog.Builder(context)) {
            setMessage(R.string.failure_booked)
            setPositiveButton(R.string.ok) { dialog, _ ->
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    companion object {
        const val TIME_PICKER_TAG = "TIME_PICKER_TAG"
        const val DATE_PICKER_TAG = "DATE_PICKER_TAG"
    }
}