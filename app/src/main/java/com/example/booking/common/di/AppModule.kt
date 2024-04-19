package com.example.booking.common.di

import android.content.Context
import androidx.room.Room
import com.example.booking.common.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Модуль приложения, предоставляющий Room БД
 */
@Module
@InstallIn(SingletonComponent::class)
internal object AppDataModule {
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME).build()
    }
}