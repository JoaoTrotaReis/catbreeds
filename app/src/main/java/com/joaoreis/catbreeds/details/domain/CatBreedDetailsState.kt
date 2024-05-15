package com.joaoreis.catbreeds.details.domain

import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed

sealed class CatBreedDetailsState {
    data object Idle: CatBreedDetailsState()
    data object Loading: CatBreedDetailsState()
    data class Loaded(val catBreed: CatBreed): CatBreedDetailsState()
    data object Error: CatBreedDetailsState()
}