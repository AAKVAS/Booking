package com.example.booking.bookings.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager

import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.booking.R
import com.example.booking.bookings.domain.BookingInteractor
import com.example.booking.bookings.domain.model.BookingEvent
import com.example.booking.common.utils.toTextTime
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class BookingNotificationService @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val bookingInteractor: BookingInteractor
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            if (bookingInteractor.isUserLogged()) {
                bookingInteractor.getComingBookingEvents().forEach {
                    showNotification(it)
                }
                bookingInteractor.deleteOldEvents()
            }
            Result.success()
        } catch (w: Exception) {
            Result.failure()
        }
    }

    private fun showNotification(bookingEvent: BookingEvent) {
        createNotificationChannel()
        val notificationText = String.format(
            applicationContext.getString(R.string.place_booked_notification),
            bookingEvent.startedAt.toTextTime(),
            bookingEvent.endedAt.toTextTime()
        )

        val builder =
                NotificationCompat.Builder(this.applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_edit_calendar_24)
            .setContentTitle(applicationContext.getString(R.string.booking_notification))
            .setContentText(notificationText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(NOTIFY_ID, builder.build())
        }
    }

    private fun createNotificationChannel() {
        val name = NOTIFICATION_CHANNEL_NAME
        val descriptionText = NOTIFICATION_CHANNEL_DESCRIPTION
        val importance = IMPORTANCE
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val WORK_NAME = "BookingNotificationWork"

        const val NOTIFY_ID = 1
        const val CHANNEL_ID = "BOOKING_CHANNEL_ID"
        const val NOTIFICATION_CHANNEL_NAME = "Уведомления о встречах"
        const val NOTIFICATION_CHANNEL_DESCRIPTION = "Данный канал уведомлений поставляет уведомления о предстоящих встречах"
        const val IMPORTANCE = NotificationManager.IMPORTANCE_HIGH
    }
}