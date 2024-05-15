package com.joaoreis.catbreeds.details.domain

import com.joaoreis.catbreeds.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class CatBreedDetailsInteractorImplementation(
    val catBreedDetailsRepository: CatBreedDetailsRepository,
    val dispatcher: CoroutineDispatcher
): CatBreedDetailsInteractor {
    val _state = MutableStateFlow<CatBreedDetailsState>(CatBreedDetailsState.Idle)
    override val state: Flow<CatBreedDetailsState> =_state


    override suspend fun loadCatBreedDetails(id: String) {
        withContext(dispatcher) {
            _state.emit(CatBreedDetailsState.Loading)
            when(val result = catBreedDetailsRepository.getCatBreed(id)) {
                is Result.Error -> _state.emit(CatBreedDetailsState.Error)
                is Result.Success -> _state.emit(CatBreedDetailsState.Loaded(result.data))
            }
        }
    }
}