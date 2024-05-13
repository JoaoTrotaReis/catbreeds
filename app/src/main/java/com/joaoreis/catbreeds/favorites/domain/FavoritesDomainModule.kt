package com.joaoreis.catbreeds.favorites.domain

import com.joaoreis.catbreeds.DefaultDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object FavoritesDomainModule {
    @Provides
    fun providesFavoriteCatBreedsInteractor(
        favoriteCatBreedsGateway: FavoriteCatBreedsGateway,
        @DefaultDispatcher dispatcher: CoroutineDispatcher
    ): FavoriteCatBreedsInteractor {
        return FavoriteCatBreedsInteractorImplementation(
            favoriteCatBreedsGateway = favoriteCatBreedsGateway,
            dispatcher = dispatcher
        )
    }
}