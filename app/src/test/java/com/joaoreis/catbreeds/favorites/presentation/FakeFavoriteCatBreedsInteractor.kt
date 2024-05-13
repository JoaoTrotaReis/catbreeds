package com.joaoreis.catbreeds.favorites.presentation

import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsInteractor
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeFavoriteCatBreedsInteractor(
    val currentState: FavoriteCatBreedsState
): FavoriteCatBreedsInteractor {
    override val state: Flow<FavoriteCatBreedsState> = MutableStateFlow(currentState)

    override suspend fun loadFavoriteCatBreeds() {  }
}