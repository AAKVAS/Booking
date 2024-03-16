package com.example.booking.services.di

import com.example.booking.common.di.NetworkModule
import com.example.booking.services.data.datasource.ServiceListApiImpl
import com.example.booking.services.data.repository.ServiceRepositoryImpl
import com.example.booking.services.domain.repository.ServiceRepository
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
    fun providesServiceApi(retrofit: Retrofit): ServiceListApiImpl {
        return retrofit.create(ServiceListApiImpl::class.java)
    }

    @Provides
    fun bindServiceRepository(api: ServiceListApiImpl): ServiceRepository = ServiceRepositoryImpl(api)
}
