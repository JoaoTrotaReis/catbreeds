package com.joaoreis.catbreeds.favorites.data

import com.joaoreis.catbreeds.IoDispatcher
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedsDao
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsGateway
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FavoritesDataModule {
    @Provides
    @Singleton
    fun providesFavoriteBreedsGateway(
        catBreedsDao: CatBreedsDao,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): FavoriteCatBreedsGateway {
        return FavoriteCatBreedsGatewayImplementation(
            catBreedsDao = catBreedsDao,
            dispatcher = dispatcher
        )
    }
}