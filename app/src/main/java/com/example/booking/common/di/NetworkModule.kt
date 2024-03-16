package com.example.booking.common.di

import com.example.booking.common.data.datasource.PingAPI
import com.example.booking.common.data.repository.RemoteRepositoryImpl
import com.example.booking.common.domain.repository.RemoteRepository
import com.example.booking.common.network.NetworkConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NetworkConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providePingAPI(retrofit: Retrofit): PingAPI {
        return retrofit.create(PingAPI::class.java)
    }

    @Provides
    fun bindRemoteRepository(api: PingAPI): RemoteRepository = RemoteRepositoryImpl(api)
}