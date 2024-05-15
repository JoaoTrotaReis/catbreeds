package com.joaoreis.catbreeds.catbreedlist.data.local

class FakeCatBreedsDao(
    val getCatBreedsResult: List<CatBreedEntity>? = null,
    val searchResult: List<CatBreedEntity>? = null,
): CatBreedsDao {
    val savedCatBreeds: MutableList<CatBreedEntity> = mutableListOf()

    override fun getCatBreeds(): List<CatBreedEntity> = getCatBreedsResult!!

    override fun saveCatBreeds(catBreeds: List<CatBreedEntity>) {
        savedCatBreeds.addAll(catBreeds)
    }

    override fun getFavoriteBreeds(): List<CatBreedEntity> = listOf()

    override fun updateCatBreed(id: String, isFavorite: Boolean) {}

    override fun findCatBreedByName(name: String): List<CatBreedEntity> = searchResult!!

    override fun findCatBreedById(id: String): CatBreedEntity {
        TODO("Not yet implemented")
    }
}