package com.joaoreis.catbreeds.catbreedlist.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed

interface BreedRemoteDataSource {
    suspend fun getBreedList(): Result<List<CatBreed>>
    suspend fun searchBreed(name: String): Result<List<CatBreed>>
}