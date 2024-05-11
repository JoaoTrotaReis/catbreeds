package com.joaoreis.catbreeds.catbreedlist.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed

class FakeLocalDataSource(
    private val getBreedsResult: Result<List<CatBreed>>
): BreedLocalDataSource {
    val storedBreeds = mutableListOf<CatBreed>()
    override fun getBreedList(): Result<List<CatBreed>> = getBreedsResult

    override fun saveBreedList(breeds: List<CatBreed>) {
        storedBreeds.addAll(breeds)
    }
}