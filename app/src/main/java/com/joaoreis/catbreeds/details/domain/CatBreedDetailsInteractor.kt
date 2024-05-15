package com.joaoreis.catbreeds.details.domain

import kotlinx.coroutines.flow.Flow

interface CatBreedDetailsInteractor {
    val state: Flow<CatBreedDetailsState>
    suspend fun loadCatBreedDetails(id: String)
}