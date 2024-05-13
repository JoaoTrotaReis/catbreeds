package com.joaoreis.catbreeds.favorites

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsGateway

class FakeFavoriteCatBreedsGateway(
    val favoriteBreedsResult: Result<List<CatBreed>>
): FavoriteCatBreedsGateway {
    override fun getFavoriteCatBreeds(): Result<List<CatBreed>> = favoriteBreedsResult
}