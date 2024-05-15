package com.joaoreis.catbreeds.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsInteractor
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteCatBreedsViewModel @Inject constructor(
    val favoriteCatBreedsInteractor: FavoriteCatBreedsInteractor
): ViewModel() {
    private val _viewState = MutableStateFlow<FavoriteCatBreedsViewState>(FavoriteCatBreedsViewState.Loading)
    val viewState: StateFlow<FavoriteCatBreedsViewState> = _viewState

    init {
        viewModelScope.launch {
            favoriteCatBreedsInteractor.loadFavoriteCatBreeds()
        }
    }

    fun subscribeToStateChanges() {
        viewModelScope.launch {
            favoriteCatBreedsInteractor.state.collect {
                handleDomainStateChanges(it)
            }
        }
    }

    private suspend fun handleDomainStateChanges(breedListState: FavoriteCatBreedsState) {
        when(breedListState) {
            FavoriteCatBreedsState.Error -> {
                _viewState.emit(FavoriteCatBreedsViewState.Error)
            }
            is FavoriteCatBreedsState.Loaded -> {
                _viewState.emit(
                    FavoriteCatBreedsViewState.Loaded(
                    breedListState.data.map {
                        FavoriteCatBreedViewItem(
                            id = it.breedId,
                            image = it.breedImage ?: "",
                            breedName = it.breedName
                        )
                    }
                ))
            }
            FavoriteCatBreedsState.Loading -> {
                _viewState.emit(FavoriteCatBreedsViewState.Loading)
            }
            FavoriteCatBreedsState.Idle -> {}
        }
    }

    fun toggleFavorite(id: String, isFavorite: Boolean) {
        viewModelScope.launch {
            favoriteCatBreedsInteractor.toggleFavorite(id, isFavorite)
        }
    }
}