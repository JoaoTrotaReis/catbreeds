package com.joaoreis.catbreeds.details.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import com.joaoreis.catbreeds.details.domain.CatBreedDetailsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CatBreedDetailsRepositoryImplementation(
    val localDataSource: BreedDetailsLocalSource,
    val remoteDataSource: BreedDetailsRemoteSource,
    val dispatcher: CoroutineDispatcher
): CatBreedDetailsRepository {
    override suspend fun getCatBreed(id: String): Result<CatBreed> = withContext(dispatcher) {
         when(val localBreedDetails = localDataSource.getBreedDetails(id)) {
            is Result.Error -> {
                when(val remoteBreedDetails = remoteDataSource.getBreedDetails(id)) {
                    is Result.Error -> Result.Error()
                    is Result.Success -> remoteBreedDetails
                }
            }
            is Result.Success -> localBreedDetails
        }
    }
}