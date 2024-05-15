package com.joaoreis.catbreeds.catbreedlist.data.remote

class FakeCatApi(
    val catBreedList: List<CatBreedDTO>? = null,
    val searchResults: List<CatBreedDTO>? = null
): CatApi {
    override suspend fun getCatBreeds(): List<CatBreedDTO> = catBreedList!!
    override suspend fun searchCatBreeds(searchTerm: String): List<CatBreedDTO> = searchResults!!
}