package com.joaoreis.catbreeds.catbreedlist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaoreis.catbreeds.catbreedlist.domain.BreedListInteractor
import com.joaoreis.catbreeds.catbreedlist.domain.BreedListState
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsInteractor
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatBreedListViewModel @Inject constructor(
    val breedListInteractor: BreedListInteractor,
    val favoriteCatBreedsInteractor: FavoriteCatBreedsInteractor
): ViewModel() {
    private val _viewState = MutableStateFlow<CatBreedListViewState>(CatBreedListViewState.Loading)
    val viewState: StateFlow<CatBreedListViewState> = _viewState

    init {
        viewModelScope.launch {
            breedListInteractor.state.collect {
                handleDomainStateChanges(it)
            }
        }
    }

    fun loadCatBreeds() {
        viewModelScope.launch {
            breedListInteractor.loadCatBreedList()
        }
    }

    fun searchCatBreeds(searchTerm: String) {
        viewModelScope.launch {
            breedListInteractor.searchCatBreeds(searchTerm)
        }
    }

    private suspend fun handleDomainStateChanges(breedListState: BreedListState) {
        when(breedListState) {
            BreedListState.Error -> {
                _viewState.emit(CatBreedListViewState.Error)
            }
            is BreedListState.Loaded -> {
                _viewState.emit(CatBreedListViewState.Loaded(
                    breedListState.data.map {
                        CatBreedViewItem(id = it.breedId, image = it.breedImage ?: "", breedName = it.breedName, isFavorite = it.isFavorite)
                    }
                ))
            }
            BreedListState.Loading -> {
                _viewState.emit(CatBreedListViewState.Loading)
            }

            BreedListState.SearchError -> _viewState.emit(CatBreedListViewState.Error)
            is BreedListState.SearchLoaded -> {
                if (breedListState.data.isEmpty()) {
                    _viewState.emit(CatBreedListViewState.EmptySearchResults)
                } else {
                    _viewState.emit(CatBreedListViewState.Loaded(
                        breedListState.data.map {
                            CatBreedViewItem(id = it.breedId, image = it.breedImage ?: "", breedName = it.breedName, isFavorite = it.isFavorite)
                        }
                    ))
                }
            }
            else -> {}
        }
    }

    fun toggleFavorite(id: String, isFavorite: Boolean) {
        viewModelScope.launch {
            favoriteCatBreedsInteractor.toggleFavorite(id, isFavorite)
            val currentViewState = _viewState.value
            if (currentViewState is CatBreedListViewState.Loaded) {
                val items = currentViewState.catBreedItems.toMutableList()
                items.replaceAll {
                    if (it.id == id) {
                        it.copy(isFavorite = isFavorite)
                    } else { it }
                }
                _viewState.emit(CatBreedListViewState.Loaded(items))
            }
        }
    }
}