package com.joaoreis.catbreeds.catbreedlist.domain

import com.joaoreis.catbreeds.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class BreedListInteractorImplementation(
    val breedRepository: BreedRepository,
    val dispatcher: CoroutineDispatcher
): BreedListInteractor {
    private val _state = MutableStateFlow<BreedListState>(BreedListState.Idle)
    override val state: Flow<BreedListState> = _state

    override suspend fun loadCatBreedList() {
        withContext(dispatcher) {
            _state.emit(BreedListState.Loading)
            when(val result = breedRepository.getCatBreeds()) {
                is Result.Error -> _state.emit(BreedListState.Error)
                is Result.Success -> _state.emit(BreedListState.Loaded(result.data))
            }
        }
    }
}