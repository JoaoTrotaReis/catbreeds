package com.joaoreis.catbreeds.catbreedlist.data.local.di

import com.joaoreis.catbreeds.catbreedlist.data.BreedLocalDataSource
import com.joaoreis.catbreeds.catbreedlist.data.local.BreedLocalDataSourceImplementation
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {
    @Provides
    fun providesLocalDataSource(
        catBreedsDao: CatBreedsDao
    ): BreedLocalDataSource {
        return BreedLocalDataSourceImplementation(
            catBreedsDao
        )
    }
}