package com.joaoreis.catbreeds.favorites.domain

import kotlinx.coroutines.flow.Flow

interface FavoriteCatBreedsInteractor {
    val state: Flow<FavoriteCatBreedsState>

    suspend fun loadFavoriteCatBreeds()
}