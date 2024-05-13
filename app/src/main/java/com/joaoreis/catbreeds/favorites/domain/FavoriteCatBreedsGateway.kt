package com.joaoreis.catbreeds.favorites.domain

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed

interface FavoriteCatBreedsGateway {
    fun getFavoriteCatBreeds(): Result<List<CatBreed>>
}