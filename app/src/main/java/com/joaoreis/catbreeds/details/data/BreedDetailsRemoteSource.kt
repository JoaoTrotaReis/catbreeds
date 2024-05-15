package com.joaoreis.catbreeds.details.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed

interface BreedDetailsRemoteSource {
    suspend fun getBreedDetails(id: String): Result<CatBreed>
}