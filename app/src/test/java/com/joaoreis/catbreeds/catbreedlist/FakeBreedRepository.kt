package com.joaoreis.catbreeds.catbreedlist

import com.joaoreis.catbreeds.catbreedlist.domain.BreedRepository
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import com.joaoreis.catbreeds.Result

class FakeBreedRepository(
    private val catBreedsResponse: Result<List<CatBreed>> = Result.Success(listOf()),
    private val searchResponse: Result<List<CatBreed>> = Result.Success(listOf())
): BreedRepository {
    override suspend fun getCatBreeds(): Result<List<CatBreed>> = catBreedsResponse
    override suspend fun searchCatBreeds(searchTerm: String): Result<List<CatBreed>> = searchResponse
}