package com.example.booking.profile.domain.model

/**
 * Статусы бронирований
 */
object BookingStatuses {
    /**
     * Бронирование активно и ещё не наступило
     */
    const val BOOKED = 1

    /**
     * Событие бронирования успешно завершено
     */
    const val ENDED = 2

    /**
     * Бронирование было отменено клиентомы
     */
    const val CANCELED_BY_CLIENT = 3

    /**
     * Бронирование было отменено организацией
     */
    const val CANCELED_BY_PROVIDER = 4
}