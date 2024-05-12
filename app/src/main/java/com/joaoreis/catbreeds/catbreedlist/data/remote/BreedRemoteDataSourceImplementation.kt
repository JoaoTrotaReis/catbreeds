package com.joaoreis.catbreeds.catbreedlist.data.remote

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.data.BreedRemoteDataSource
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed

class BreedRemoteDataSourceImplementation(
    private val catApi: CatApi
): BreedRemoteDataSource {
    override suspend fun getBreedList(): Result<List<CatBreed>> {
        return try {
            val catBreeds = catApi.getCatBreeds().map {
                it.toDomainModel()
            }
            Result.Success(catBreeds)
        } catch (e: Exception) {
            Result.Error()
        }
    }
}