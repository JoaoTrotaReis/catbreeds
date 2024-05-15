package com.joaoreis.catbreeds.catbreedlist.data.remote

class FakeCatApi(
    val catBreedList: List<CatBreedDTO>? = null,
    val searchResults: List<CatBreedDTO>? = null,
    val findResult: CatBreedDTO? = null
): CatApi {
    override suspend fun getCatBreeds(): List<CatBreedDTO> = catBreedList!!
    override suspend fun searchCatBreeds(searchTerm: String): List<CatBreedDTO> = searchResults!!
    override suspend fun getCatBreedById(id: String): CatBreedDTO = findResult!!
}