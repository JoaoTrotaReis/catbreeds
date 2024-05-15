package com.joaoreis.catbreeds.details.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.data.remote.CatApi
import com.joaoreis.catbreeds.catbreedlist.data.remote.toDomainModel
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed


class CatBreedDetailsRemoteSourceImplementation(
    private val catApi: CatApi
): BreedDetailsRemoteSource {
    override suspend fun getBreedDetails(id: String): Result<CatBreed> {
        return try {
            Result.Success(catApi.getCatBreedById(id).toDomainModel())
        } catch (e: Exception) {
            Result.Error()
        }
    }
}