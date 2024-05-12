package com.joaoreis.catbreeds.catbreedlist.presentation

import com.joaoreis.catbreeds.catbreedlist.domain.BreedListInteractor
import com.joaoreis.catbreeds.catbreedlist.domain.BreedListState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeBreedListInteractor(
    val currentState: BreedListState
): BreedListInteractor {
    override val state: Flow<BreedListState> = MutableStateFlow(currentState)

    override suspend fun loadCatBreedList() {}
}