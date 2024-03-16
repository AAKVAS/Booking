package com.example.booking.bookings.di

import com.example.booking.bookings.data.datasource.BookingAPI
import com.example.booking.bookings.data.repository.BookingRepositoryImpl
import com.example.booking.bookings.domain.model.Booking
import com.example.booking.bookings.domain.repository.BookingRepository
import com.example.booking.common.di.NetworkModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
object BookingModule {
    @Provides
    @Singleton
    fun providesBookingAPI(retrofit: Retrofit): BookingAPI {
        //return retrofit.create(BookingAPI::class.java)
        return object : BookingAPI {
            override suspend fun bookPlace(userLogin: String, booking: Booking) {

            }
        }
    }

    @Provides
    fun bindServiceRepository(api: BookingAPI): BookingRepository = BookingRepositoryImpl(api)
}
