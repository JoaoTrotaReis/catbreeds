package com.joaoreis.catbreeds.favorites.domain

import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed

sealed class FavoriteCatBreedsState {
    data object Idle: FavoriteCatBreedsState()
    data object Loading: FavoriteCatBreedsState()
    data class Loaded(val data: List<CatBreed>): FavoriteCatBreedsState()
    data object Error: FavoriteCatBreedsState()
}