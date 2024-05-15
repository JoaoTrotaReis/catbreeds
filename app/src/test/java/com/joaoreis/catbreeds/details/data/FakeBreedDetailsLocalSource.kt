package com.joaoreis.catbreeds.details.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import com.joaoreis.catbreeds.details.data.BreedDetailsLocalSource

class FakeBreedDetailsLocalSource(
    val detailsResult: Result<CatBreed>
): BreedDetailsLocalSource {
    override suspend fun getBreedDetails(id: String): Result<CatBreed> = detailsResult
}