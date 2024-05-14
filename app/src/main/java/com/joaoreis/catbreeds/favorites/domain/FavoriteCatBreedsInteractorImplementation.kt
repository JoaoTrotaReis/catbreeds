package com.joaoreis.catbreeds.favorites.domain

import com.joaoreis.catbreeds.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class FavoriteCatBreedsInteractorImplementation(
    val favoriteCatBreedsGateway: FavoriteCatBreedsGateway,
    val dispatcher: CoroutineDispatcher
): FavoriteCatBreedsInteractor {
    private val _state = MutableStateFlow<FavoriteCatBreedsState>(FavoriteCatBreedsState.Idle)
    override val state: Flow<FavoriteCatBreedsState> = _state

    override suspend fun loadFavoriteCatBreeds() {
        withContext(dispatcher) {
            _state.emit(FavoriteCatBreedsState.Loading)
            when(val result = favoriteCatBreedsGateway.getFavoriteCatBreeds()) {
                is Result.Error -> _state.emit(FavoriteCatBreedsState.Error)
                is Result.Success -> _state.emit(FavoriteCatBreedsState.Loaded(result.data))
            }
        }
    }

    override suspend fun toggleFavorite(id: String, isFavorite: Boolean) {
        favoriteCatBreedsGateway.toggleFavorite(id, isFavorite)
        loadFavoriteCatBreeds()
    }
}