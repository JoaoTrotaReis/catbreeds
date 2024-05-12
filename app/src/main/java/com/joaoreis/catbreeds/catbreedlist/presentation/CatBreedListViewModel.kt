package com.joaoreis.catbreeds.catbreedlist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaoreis.catbreeds.catbreedlist.domain.BreedListInteractor
import com.joaoreis.catbreeds.catbreedlist.domain.BreedListState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CatBreedListViewModel(
    val breedListInteractor: BreedListInteractor
): ViewModel() {
    private val _viewState = MutableStateFlow<CatBreedListViewState>(CatBreedListViewState.Loading)
    val viewState: Flow<CatBreedListViewState> = _viewState

    init {
        viewModelScope.launch {
            breedListInteractor.loadCatBreedList()
            breedListInteractor.state.collect {
                handleDomainStateChanges(it)
            }
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
                        CatBreedViewItem(image = it.breedImage, breedName = it.breedName)
                    }
                ))
            }
            BreedListState.Loading -> {
                _viewState.emit(CatBreedListViewState.Loading)
            }
            BreedListState.Idle -> {}
        }
    }
}