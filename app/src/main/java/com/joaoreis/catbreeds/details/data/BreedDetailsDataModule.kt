package com.joaoreis.catbreeds.details.data

import com.joaoreis.catbreeds.IoDispatcher
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedsDao
import com.joaoreis.catbreeds.catbreedlist.data.remote.CatApi
import com.joaoreis.catbreeds.details.domain.CatBreedDetailsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(SingletonComponent::class)
object BreedDetailsDataModule {
    @Provides
    fun providesLocalDataSource(
        catBreedsDao: CatBreedsDao
    ): BreedDetailsLocalSource {
        return BreedDetailsLocalSourceImplementation(catBreedsDao)
    }

    @Provides
    fun providesRemoteDataSource(
        catApi: CatApi
    ): BreedDetailsRemoteSource {
        return CatBreedDetailsRemoteSourceImplementation(catApi)
    }

    @Provides
    fun providesBreedDetailsRepository(
        localSource: BreedDetailsLocalSource,
        remoteSource: BreedDetailsRemoteSource,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): CatBreedDetailsRepository {
        return CatBreedDetailsRepositoryImplementation(
            localDataSource = localSource,
            remoteDataSource = remoteSource,
            dispatcher = dispatcher
        )
    }
}