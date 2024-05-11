package com.joaoreis.catbreeds.catbreedlist.domain

import com.joaoreis.catbreeds.Result

interface BreedRepository {
    suspend fun getCatBreeds(): Result<List<CatBreed>>
}