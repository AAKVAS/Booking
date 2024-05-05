package com.example.booking.auth.di

import android.content.Context
import com.example.booking.auth.data.datasource.CloudLoginAPI
import com.example.booking.auth.data.datasource.LocalLoginDatasource
import com.example.booking.auth.data.datasource.LocalLoginDatasourceImpl
import com.example.booking.auth.data.repository.LoginRepositoryImpl
import com.example.booking.auth.domain.repository.LoginRepository
import com.example.booking.common.di.NetworkModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * DI-Модуль для авторизации
 */
@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Singleton
    @Provides
    fun provideLocalLoginDatastore(@ApplicationContext context: Context): LocalLoginDatasource
            = LocalLoginDatasourceImpl(context)

    @Singleton
    @Provides
    fun provideCloudLoginDatastore(retrofit: Retrofit): CloudLoginAPI {
        return retrofit.create(CloudLoginAPI::class.java)
    }

    @Provides
    fun bindLoginRepository(
        localLoginDatasource: LocalLoginDatasource,
        api: CloudLoginAPI,
    ): LoginRepository =
        LoginRepositoryImpl(localLoginDatasource, api)
}