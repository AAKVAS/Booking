package com.example.booking.profile.di

import com.example.booking.common.di.NetworkModule
import com.example.booking.profile.data.datasource.ProfileAPI
import com.example.booking.profile.data.repository.ProfileRepositoryImpl
import com.example.booking.profile.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class ProfileModule {
    @Provides
    @Singleton
    fun providesProfileAPI(retrofit: Retrofit): ProfileAPI {
        return retrofit.create(ProfileAPI::class.java)
    }

    @Provides
    fun bindProfileRepository(api: ProfileAPI): ProfileRepository = ProfileRepositoryImpl(api)
}