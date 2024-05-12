package com.joaoreis.catbreeds.catbreedlist.data.remote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BaseUrlModule {
    @Provides
    fun providesBaseUrl(): String = "https://api.thecatapi.com/v1/"
}