package com.joaoreis.catbreeds.details.domain

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import com.joaoreis.catbreeds.details.domain.CatBreedDetailsRepository

class FakeCatBreedDetailsRepository(
    val catBreedResult: Result<CatBreed>
): CatBreedDetailsRepository {
    override suspend fun getCatBreed(id: String): Result<CatBreed> = catBreedResult
}