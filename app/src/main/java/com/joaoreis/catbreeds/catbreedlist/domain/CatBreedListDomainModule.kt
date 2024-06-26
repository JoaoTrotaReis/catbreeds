package com.joaoreis.catbreeds.catbreedlist.domain

import com.joaoreis.catbreeds.DefaultDispatcher
import com.joaoreis.catbreeds.IoDispatcher
import com.joaoreis.catbreeds.catbreedlist.domain.BreedListInteractor
import com.joaoreis.catbreeds.catbreedlist.domain.BreedListInteractorImplementation
import com.joaoreis.catbreeds.catbreedlist.domain.BreedRepository
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsGateway
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object CatBreedListDomainModule {
    @Provides
    fun provideBreedListInteractor(
        breedRepository: BreedRepository,
        favoriteCatBreedsGateway: FavoriteCatBreedsGateway,
        @DefaultDispatcher dispatcher: CoroutineDispatcher
    ): BreedListInteractor {
        return BreedListInteractorImplementation(
            breedRepository = breedRepository,
            favoriteCatBreedsGateway = favoriteCatBreedsGateway,
            dispatcher = dispatcher
        )
    }
}