package com.example.booking.common.data

sealed interface LoadingState<in T> {
    data class Success<T>(val body: T): LoadingState<T>
    data class Failure<T>(val error: Exception): LoadingState<T>
    data object Loading: LoadingState<Any>
}