package com.joaoreis.catbreeds.catbreedlist.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed

class FakeRemoteDataSource(
    val getBreedsResult: Result<List<CatBreed>>
): BreedRemoteDataSource {
    override fun getBreedList(): Result<List<CatBreed>> = getBreedsResult
}