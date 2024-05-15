package com.joaoreis.catbreeds.catbreedlist.domain

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsGateway
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BreedListInteractorImplementation(
    val breedRepository: BreedRepository,
    val favoriteCatBreedsGateway: FavoriteCatBreedsGateway,
    val dispatcher: CoroutineDispatcher,
    initialState: BreedListState = BreedListState.Idle
): BreedListInteractor {
    private val _state = MutableStateFlow(initialState)
    override val state: Flow<BreedListState> = _state

    init {
        CoroutineScope(dispatcher).launch {
            favoriteCatBreedsGateway.favoriteUpdates.collect { favoriteUpdate ->
                val currentState = _state.value
                _state.emit(updateFavorite(currentState, favoriteUpdate))
            }
        }
    }

    override suspend fun loadCatBreedList() {
        withContext(dispatcher) {
            _state.emit(BreedListState.Loading)
            when(val result = breedRepository.getCatBreeds()) {
                is Result.Error -> _state.emit(BreedListState.Error)
                is Result.Success -> _state.emit(BreedListState.Loaded(result.data))
            }
        }
    }

    override suspend fun searchCatBreeds(searchTearm: String) {
        withContext(dispatcher) {
            _state.emit(BreedListState.Loading)
            when(val result = breedRepository.searchCatBreeds(searchTearm)) {
                is Result.Error -> _state.emit(BreedListState.SearchError)
                is Result.Success -> _state.emit(BreedListState.SearchLoaded(result.data))
            }
        }
    }

    private fun updateFavorite(currentState: BreedListState, favoriteUpdate: FavoriteUpdate): BreedListState {
        return when(currentState) {
            is BreedListState.Loaded -> {
                val items = currentState.data.map {
                    if (it.breedId == favoriteUpdate.id) {
                        it.copy(isFavorite = favoriteUpdate.isFavorite)
                    } else { it }
                }
                BreedListState.Loaded(items)
            }
            is BreedListState.SearchLoaded -> {
                val items = currentState.data.map {
                    if (it.breedId == favoriteUpdate.id) {
                        it.copy(isFavorite = favoriteUpdate.isFavorite)
                    } else { it }
                }
                BreedListState.SearchLoaded(items)
            }
            else -> currentState
        }
    }
}