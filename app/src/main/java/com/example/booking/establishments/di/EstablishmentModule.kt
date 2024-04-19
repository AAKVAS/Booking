package com.example.booking.establishments.di

import com.example.booking.common.di.NetworkModule
import com.example.booking.establishments.data.datasource.EstablishmentListAPI
import com.example.booking.establishments.data.repository.EstablishmentRepositoryImpl
import com.example.booking.establishments.domain.repository.EstablishmentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * DI-модуль заведений
 */
@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
object EstablishmentModule {
    @Provides
    @Singleton
    fun providesEstablishmentApi(retrofit: Retrofit): EstablishmentListAPI {
        return retrofit.create(EstablishmentListAPI::class.java)
    }

    @Provides
    fun bindEstablishmentRepository(api: EstablishmentListAPI): EstablishmentRepository =
        EstablishmentRepositoryImpl(api)
}
