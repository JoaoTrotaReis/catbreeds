package com.joaoreis.catbreeds.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaoreis.catbreeds.details.domain.CatBreedDetailsInteractor
import com.joaoreis.catbreeds.details.domain.CatBreedDetailsState
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatBreedDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    val catBreedDetailsInteractor: CatBreedDetailsInteractor,
    val favoriteCatBreedsInteractor: FavoriteCatBreedsInteractor
) : ViewModel() {
    private val _viewState =
        MutableStateFlow<CatBreedDetailsViewState>(CatBreedDetailsViewState.Loading)
    val viewState: StateFlow<CatBreedDetailsViewState> = _viewState

    private val id = savedStateHandle.get<String>("id")!!

    fun loadDetails() {
        viewModelScope.launch {
            catBreedDetailsInteractor.loadCatBreedDetails(id)
            catBreedDetailsInteractor.state.collect {
                handleDomainStateChanges(it)
            }
        }
    }

    fun onFavoriteToggle(id: String, isFavorite: Boolean) {
        viewModelScope.launch {
            favoriteCatBreedsInteractor.toggleFavorite(id, isFavorite)
            val currentState = viewState.value
            if (currentState is CatBreedDetailsViewState.Loaded) {
                _viewState.emit(
                    CatBreedDetailsViewState.Loaded(
                        currentState.catBreedDetails.copy(
                            isFavorite = isFavorite
                        )
                    )
                )
            }
        }
    }

    private suspend fun handleDomainStateChanges(catBreedDetailsState: CatBreedDetailsState) {
        when (catBreedDetailsState) {
            CatBreedDetailsState.Error -> _viewState.emit(CatBreedDetailsViewState.Error)
            is CatBreedDetailsState.Loaded -> {
                val catBreedDetails = CatBreedDetails(
                    catBreedDetailsState.catBreed.breedId,
                    catBreedDetailsState.catBreed.breedImage ?: "",
                    catBreedDetailsState.catBreed.breedName,
                    catBreedDetailsState.catBreed.description,
                    catBreedDetailsState.catBreed.origin,
                    catBreedDetailsState.catBreed.temperament,
                    catBreedDetailsState.catBreed.isFavorite,
                )
                _viewState.emit(CatBreedDetailsViewState.Loaded(catBreedDetails))
            }

            CatBreedDetailsState.Loading -> _viewState.emit(CatBreedDetailsViewState.Loading)
            CatBreedDetailsState.Idle -> {}
        }
    }
}