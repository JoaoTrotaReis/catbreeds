package com.joaoreis.catbreeds.catbreedlist.data

import com.joaoreis.catbreeds.IoDispatcher
import com.joaoreis.catbreeds.catbreedlist.domain.BreedRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object CatBreedDataModule {
    @Provides
    fun providesBreedRepository(
        localDataSource: BreedLocalDataSource,
        remoteDataSource: BreedRemoteDataSource,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): BreedRepository {
        return BreedRepositoryImplementation(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            dispatcher = dispatcher
        )
    }
}