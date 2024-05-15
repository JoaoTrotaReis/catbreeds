package com.joaoreis.catbreeds.catbreedlist.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed

class FakeLocalDataSource(
    private val getBreedsResult: Result<List<CatBreed>> = Result.Success(listOf()),
    private val searchResult: Result<List<CatBreed>> = Result.Success(listOf())
): BreedLocalDataSource {
    val storedBreeds = mutableListOf<CatBreed>()
    override fun getBreedList(): Result<List<CatBreed>> = getBreedsResult

    override fun saveBreedList(breeds: List<CatBreed>) {
        storedBreeds.addAll(breeds)
    }

    override fun searchBreed(name: String): Result<List<CatBreed>> = searchResult
}