package com.joaoreis.catbreeds.favorites.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsGateway

class FakeFavoriteCatBreedsGateway(
    val favoriteBreedsResult: Result<List<CatBreed>>
): FavoriteCatBreedsGateway {
    val favorites = HashMap<String, Boolean>()
    override suspend fun getFavoriteCatBreeds(): Result<List<CatBreed>> = favoriteBreedsResult
    override suspend fun toggleFavorite(id: String, isFavorite: Boolean) { favorites[id] = isFavorite }
}