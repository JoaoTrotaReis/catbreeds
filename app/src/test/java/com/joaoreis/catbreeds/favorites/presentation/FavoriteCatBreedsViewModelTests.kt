package com.joaoreis.catbreeds.favorites.presentation

import app.cash.turbine.test
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteCatBreedsViewModelTests {
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
    fun `Given the favorite cat breeds were requested When the interactor emits a loading state Then emit a load view state`() = runTest {
        val interactor = FakeFavoriteCatBreedsInteractor(
            currentState = FavoriteCatBreedsState.Loading
        )

        val viewModel = FavoriteCatBreedsViewModel(
            interactor
        )

        viewModel.viewState.test {
            assertTrue(awaitItem() is FavoriteCatBreedsViewState.Loading)
        }
    }

    @Test
    fun `Given the favorite cat breeds were requested When the interactor emits an error state Then emit an error view state`() = runTest {
        val interactor = FakeFavoriteCatBreedsInteractor(
            currentState = FavoriteCatBreedsState.Error
        )

        val viewModel = FavoriteCatBreedsViewModel(
            interactor
        )

        viewModel.subscribeToStateChanges()

        viewModel.viewState.test {
            assertTrue(awaitItem() is FavoriteCatBreedsViewState.Error)
        }
    }

    @Test
    fun `Given the favorite cat breeds were requested When the interactor emits a loaded state Then emit an loaded view state`() = runTest {
        val interactor = FakeFavoriteCatBreedsInteractor(
            currentState = FavoriteCatBreedsState.Loaded(
                listOf(
                    CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("temperament"), false),
                    CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("temperament"), false)
                )
            )
        )

        val expectedViewState = FavoriteCatBreedsViewState.Loaded(
            listOf(
                FavoriteCatBreedViewItem("id1", "image1", "name1"),
                FavoriteCatBreedViewItem("id2","image2", "name2")
            )
        )

        val viewModel = FavoriteCatBreedsViewModel(
            interactor
        )

        viewModel.subscribeToStateChanges()

        viewModel.viewState.test {
            assertEquals(expectedViewState, awaitItem())
        }
    }
}