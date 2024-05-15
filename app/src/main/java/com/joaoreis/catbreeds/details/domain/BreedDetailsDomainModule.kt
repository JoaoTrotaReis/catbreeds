package com.joaoreis.catbreeds.details.domain

import com.joaoreis.catbreeds.DefaultDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
class BreedDetailsDomainModule {
    @Provides
    fun providesBreedDetailsInteractor(
        breedDetailsRepository: CatBreedDetailsRepository,
        @DefaultDispatcher dispatcher: CoroutineDispatcher
    ): CatBreedDetailsInteractor {
        return CatBreedDetailsInteractorImplementation(
            breedDetailsRepository, dispatcher
        )
    }
}