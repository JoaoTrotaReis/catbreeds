package com.joaoreis.catbreeds.catbreedlist.domain

import kotlinx.coroutines.flow.Flow

interface BreedListInteractor {
    val state: Flow<BreedListState>

    suspend fun loadCatBreedList()
}
