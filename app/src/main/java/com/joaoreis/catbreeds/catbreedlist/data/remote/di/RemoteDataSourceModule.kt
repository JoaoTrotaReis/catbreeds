package com.joaoreis.catbreeds.catbreedlist.data.remote.di

import com.joaoreis.catbreeds.catbreedlist.data.BreedRemoteDataSource
import com.joaoreis.catbreeds.catbreedlist.data.remote.BreedRemoteDataSourceImplementation
import com.joaoreis.catbreeds.catbreedlist.data.remote.CatApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {
    @Provides
    fun providesRemoteDataSource(
        catApi: CatApi
    ): BreedRemoteDataSource {
        return BreedRemoteDataSourceImplementation(catApi)
    }
}