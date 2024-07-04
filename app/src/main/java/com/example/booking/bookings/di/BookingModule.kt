package com.example.booking.bookings.di

import android.content.Context
import androidx.room.Room
import com.example.booking.bookings.data.datasource.BookingAPI
import com.example.booking.bookings.data.datasource.BookingEventDao
import com.example.booking.bookings.data.repository.BookingRepositoryImpl
import com.example.booking.bookings.domain.repository.BookingRepository
import com.example.booking.common.data.AppDatabase
import com.example.booking.common.data.AppDatabase.Companion.DATABASE_NAME
import com.example.booking.common.di.AppDataModule
import com.example.booking.common.di.NetworkModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * DI-модуль бронированя
 */
@Module(includes = [NetworkModule::class, AppDataModule::class])
@InstallIn(SingletonComponent::class)
object BookingModule {
    @Provides
    fun provideBookingEventDao(database: AppDatabase): BookingEventDao = database.getBookingEventDao()

    @Provides
    @Singleton
    fun providesBookingAPI(retrofit: Retrofit): BookingAPI {
        return retrofit.create(BookingAPI::class.java)
    }

    @Provides
    fun bindServiceRepository(api: BookingAPI, bookingEventDao: BookingEventDao): BookingRepository =
        BookingRepositoryImpl(api, bookingEventDao)
}
