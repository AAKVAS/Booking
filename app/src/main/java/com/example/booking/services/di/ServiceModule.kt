package com.example.booking.services.di

import com.example.booking.common.di.NetworkModule
import com.example.booking.services.data.datasource.ServiceListAPI
import com.example.booking.services.data.datasource.ServiceListAPIMock
import com.example.booking.services.data.datasource.ServiceListApiImpl
import com.example.booking.services.data.datasource.ServicePagingSource
import com.example.booking.services.data.repository.ServiceRepositoryImpl
import com.example.booking.services.domain.repository.ServiceRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun providesServiceApi(retrofit: Retrofit): ServiceListAPI {
        return retrofit.create(ServiceListApiImpl::class.java)
    }

    @Provides
    fun bindServiceRepository(api: ServiceListAPI): ServiceRepository = ServiceRepositoryImpl(api)
}