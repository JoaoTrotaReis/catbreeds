package com.joaoreis.catbreeds.details.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed

interface BreedDetailsLocalSource {
    suspend fun getBreedDetails(id: String): Result<CatBreed>
}