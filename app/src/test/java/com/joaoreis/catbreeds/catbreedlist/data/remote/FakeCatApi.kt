package com.joaoreis.catbreeds.catbreedlist.data.remote

class FakeCatApi(
    val catBreedList: List<CatBreedDTO>? = null
): CatApi {
    override fun getCatBreeds(): List<CatBreedDTO> = catBreedList!!
}