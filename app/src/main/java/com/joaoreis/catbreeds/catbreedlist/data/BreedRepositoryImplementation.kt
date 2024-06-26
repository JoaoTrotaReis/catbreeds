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
            is Result.Success -> {
                if(localBreedList.data.isEmpty()) {
                    loadAndSaveRemoteData()
                } else localBreedList

            }
            is Result.Error -> {
                loadAndSaveRemoteData()
            }
        }
    }

    override suspend fun searchCatBreeds(searchTerm: String): Result<List<CatBreed>> {
        return when(val localBreedList = localDataSource.searchBreed(searchTerm)) {
            is Result.Success -> {
                if(localBreedList.data.isEmpty()) {
                    searchRemoteData(searchTerm)
                } else localBreedList

            }
            is Result.Error -> {
                searchRemoteData(searchTerm)
            }
        }
    }

    private suspend fun searchRemoteData(searchTerm: String): Result<List<CatBreed>> {
        return when(val remoteBreedList = remoteDataSource.searchBreed(searchTerm)) {
            is Result.Error -> Result.Error()
            is Result.Success -> Result.Success(remoteBreedList.data)
        }
    }

    private suspend fun loadAndSaveRemoteData(): Result<List<CatBreed>> {
        return when(val remoteBreedList = remoteDataSource.getBreedList()) {
            is Result.Error -> Result.Error()
            is Result.Success -> {
                localDataSource.saveBreedList(remoteBreedList.data)
                Result.Success(remoteBreedList.data)
            }
        }
    }
}