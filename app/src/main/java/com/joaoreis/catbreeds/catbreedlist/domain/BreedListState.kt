package com.joaoreis.catbreeds.catbreedlist.domain

sealed class BreedListState {
    data object Idle: BreedListState()
    data object Loading: BreedListState()
    data class Loaded(val data: List<CatBreed>): BreedListState()
    data object Error: BreedListState()
    data class SearchLoaded(val data: List<CatBreed>): BreedListState()
    data object SearchError: BreedListState()
}