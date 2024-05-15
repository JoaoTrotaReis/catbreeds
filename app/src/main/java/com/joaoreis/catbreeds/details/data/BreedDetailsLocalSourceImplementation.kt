package com.joaoreis.catbreeds.details.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedsDao
import com.joaoreis.catbreeds.catbreedlist.data.local.toDomainModel
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed

class BreedDetailsLocalSourceImplementation(
    private val catBreedsDao: CatBreedsDao
): BreedDetailsLocalSource {
    override suspend fun getBreedDetails(id: String): Result<CatBreed> {
        return try {
           Result.Success(catBreedsDao.findCatBreedById(id).toDomainModel())
        } catch (e: Exception) {
            Result.Error()
        }
    }
}