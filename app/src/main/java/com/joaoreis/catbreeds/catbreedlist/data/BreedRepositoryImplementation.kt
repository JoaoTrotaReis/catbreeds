package com.joaoreis.catbreeds.catbreedlist.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.BreedRepository
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class BreedRepositoryImplementation(
    private val localDataSource: BreedLocalDataSource,
    private val remoteDataSource: BreedRemoteDataSource,
    private val dispatcher: CoroutineDispatcher
): BreedRepository {
    override suspend fun getCatBreeds(): Result<List<CatBreed>> = withContext(dispatcher){
        when(val localBreedList = localDataSource.getBreedList()) {
            is Result.Success -> localBreedList
            is Result.Error -> {
                when(val remoteBreedList = remoteDataSource.getBreedList()) {
                    is Result.Error -> Result.Error()
                    is Result.Success -> {
                        localDataSource.saveBreedList(remoteBreedList.data)
                        Result.Success(remoteBreedList.data)
                    }
                }
            }
        }
    }
}