package com.joaoreis.catbreeds.details.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import com.joaoreis.catbreeds.details.domain.CatBreedDetailsState
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsState
import com.joaoreis.catbreeds.favorites.presentation.FakeFavoriteCatBreedsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CatBreedDetailsViewModelTests {
    @Before
    fun setup() {
        val testDispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Given the cat breed details were requested When the interactor emits a loading state Then emit a loaded view state`() = runTest {
        val interactor = FakeCatBreedDetailsInteractor(
            currentState = CatBreedDetailsState.Loading
        )

        val savedStateHandle = SavedStateHandle().apply {
            set("id", "id1")
        }

        val viewModel = CatBreedDetailsViewModel(
            catBreedDetailsInteractor = interactor,
            favoriteCatBreedsInteractor = FakeFavoriteCatBreedsInteractor(FavoriteCatBreedsState.Idle),
            savedStateHandle = savedStateHandle
        )

        viewModel.loadDetails()

        viewModel.viewState.test {
           assertTrue(awaitItem() is CatBreedDetailsViewState.Loading)
        }
    }

    @Test
    fun `Given the cat breed details were requested When the interactor emits an error state Then emit an error view state`() = runTest {
        val interactor = FakeCatBreedDetailsInteractor(
            currentState = CatBreedDetailsState.Error
        )

        val savedStateHandle = SavedStateHandle().apply {
            set("id", "id1")
        }

        val viewModel = CatBreedDetailsViewModel(
            catBreedDetailsInteractor = interactor,
            favoriteCatBreedsInteractor = FakeFavoriteCatBreedsInteractor(FavoriteCatBreedsState.Idle),
            savedStateHandle = savedStateHandle
        )

        viewModel.loadDetails()

        viewModel.viewState.test {
            assertTrue(awaitItem() is CatBreedDetailsViewState.Error)
        }
    }

    @Test
    fun `Given the cat breed details were requested When the interactor emits a loaded state Then emit an loaded view state`() = runTest {
        val interactor = FakeCatBreedDetailsInteractor(
            currentState = CatBreedDetailsState.Loaded(
                CatBreed("id", "name", "image", "origin", "description", listOf("temperament"), false)
            )
        )

        val savedStateHandle = SavedStateHandle().apply {
            set("id", "id")
        }

        val expectedViewState = CatBreedDetailsViewState.Loaded(
            CatBreedDetails("id", "image", "name", "description", "origin", listOf("temperament"), false)
        )

        val viewModel = CatBreedDetailsViewModel(
            catBreedDetailsInteractor = interactor,
            favoriteCatBreedsInteractor = FakeFavoriteCatBreedsInteractor(FavoriteCatBreedsState.Idle),
            savedStateHandle = savedStateHandle
        )

        viewModel.loadDetails()

        viewModel.viewState.test {
            Assert.assertEquals(expectedViewState, awaitItem())
        }
    }
}