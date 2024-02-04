package com.example.booking.auth.di

import android.content.Context
import com.example.booking.auth.data.datasource.CloudLoginDatasource
import com.example.booking.auth.data.datasource.CloudLoginDatasourceImpl
import com.example.booking.auth.data.datasource.LocalLoginDatasource
import com.example.booking.auth.data.datasource.LocalLocalLoginDatasourceImpl
import com.example.booking.auth.data.repository.LoginRepositoryImpl
import com.example.booking.auth.domain.repository.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthModule {
    @Binds
    fun bindLoginRepository(loginRepository: LoginRepositoryImpl): LoginRepository
}

@Module
@InstallIn(SingletonComponent::class)
object LoginDataModule {
    @Singleton
    @Provides
    fun provideLocalLoginDatastore(@ApplicationContext context: Context): LocalLoginDatasource
        = LocalLocalLoginDatasourceImpl(context)

    @Singleton
    @Provides
    fun provideCloudLoginDatastore(): CloudLoginDatasource = CloudLoginDatasourceImpl()
}