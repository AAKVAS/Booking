package com.example.booking.bookings.ui.model

/**
 * Состояние условий для бронирования
 */
enum class BookingValidationStatus {
    /**
     * Данные верны
     */
    VALID,
    /**
     * Некорректное время для бронирования
     */
    WRONG_TIME,
    /**
     * Не выбрано место для бронирования
     */
    PLACE_NOT_SELECTED
}