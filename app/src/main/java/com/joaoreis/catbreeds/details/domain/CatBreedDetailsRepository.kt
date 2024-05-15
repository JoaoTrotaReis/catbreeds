package com.joaoreis.catbreeds.details.domain

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed

interface CatBreedDetailsRepository {
    suspend fun getCatBreed(id: String): Result<CatBreed>
}