package com.example.booking.common.data

/**
 * Состояние загрузки
 */
sealed interface LoadingState<in T> {
    /**
     *  Данные загрузились
     */
    data class Success<T>(val body: T): LoadingState<T>

    /**
     * Произошла ошибка при загрузке данных
     */
    data class Failure<T>(val error: Exception): LoadingState<T>

    /**
     * Данные загружаются
     */
    data object Loading: LoadingState<Any>
}