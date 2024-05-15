package com.joaoreis.catbreeds.details.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import com.joaoreis.catbreeds.details.data.BreedDetailsRemoteSource

class FakeBreedDetailsRemoteSource(
    val detailsResponse: Result<CatBreed>
): BreedDetailsRemoteSource {
    override suspend fun getBreedDetails(id: String): Result<CatBreed> = detailsResponse
}