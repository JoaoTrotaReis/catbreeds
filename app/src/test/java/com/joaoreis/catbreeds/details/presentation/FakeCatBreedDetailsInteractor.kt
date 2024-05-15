package com.joaoreis.catbreeds.details.presentation

import com.joaoreis.catbreeds.details.domain.CatBreedDetailsInteractor
import com.joaoreis.catbreeds.details.domain.CatBreedDetailsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeCatBreedDetailsInteractor(
    currentState: CatBreedDetailsState
): CatBreedDetailsInteractor {
    override val state: Flow<CatBreedDetailsState> = MutableStateFlow(currentState)

    override suspend fun loadCatBreedDetails(id: String) {}
}