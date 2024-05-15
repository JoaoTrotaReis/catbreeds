package com.joaoreis.catbreeds.catbreedlist.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed

class FakeRemoteDataSource(
    val getBreedsResult: Result<List<CatBreed>> = Result.Success(listOf()),
    val searchResult: Result<List<CatBreed>> = Result.Success(listOf()),
): BreedRemoteDataSource {
    override suspend fun getBreedList(): Result<List<CatBreed>> = getBreedsResult
    override suspend fun searchBreed(name: String): Result<List<CatBreed>> = searchResult
}