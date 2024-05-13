package com.joaoreis.catbreeds.catbreedlist.data.local

class FakeCatBreedsDao(
    val getCatBreedsResult: List<CatBreedEntity>? = null
): CatBreedsDao {
    val savedCatBreeds: MutableList<CatBreedEntity> = mutableListOf()

    override fun getCatBreeds(): List<CatBreedEntity> = getCatBreedsResult!!

    override fun saveCatBreeds(catBreeds: List<CatBreedEntity>) {
        savedCatBreeds.addAll(catBreeds)
    }

    override fun getFavoriteBreeds(): List<CatBreedEntity> {
        TODO("Not yet implemented")
    }
}