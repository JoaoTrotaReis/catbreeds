package com.joaoreis.catbreeds.util

import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedEntity
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedsDao

class FakeCatBreedsDao(
    val getCatBreedsResult: List<CatBreedEntity>? = null,
    val searchResult: List<CatBreedEntity>? = null,
    val findResult: CatBreedEntity? = null,
    var favoritesResult: List<CatBreedEntity>? = null
): CatBreedsDao {
    val savedCatBreeds: MutableList<CatBreedEntity> = mutableListOf()

    override fun getCatBreeds(): List<CatBreedEntity> = getCatBreedsResult!!

    override fun saveCatBreeds(catBreeds: List<CatBreedEntity>) {
        savedCatBreeds.addAll(catBreeds)
    }

    override fun getFavoriteBreeds(): List<CatBreedEntity> = favoritesResult!!

    override fun updateCatBreed(id: String, isFavorite: Boolean) {
        if(!isFavorite) {
            favoritesResult = favoritesResult?.filter { it.id != id }
        }
    }

    override fun findCatBreedByName(name: String): List<CatBreedEntity> = searchResult!!

    override fun findCatBreedById(id: String): CatBreedEntity = findResult!!
}