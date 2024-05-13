package com.joaoreis.catbreeds.catbreedlist.presentation

import app.cash.turbine.test
import com.joaoreis.catbreeds.catbreedlist.domain.BreedListState
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CatBreedListViewModelTests {


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
    fun `Given the cat breed list was requested When the interactor emits a loading state Then emit a load view state`() = runTest {
        val interactor = FakeBreedListInteractor(
            currentState = BreedListState.Loading
        )

        val viewModel = CatBreedListViewModel(
            interactor
        )

        viewModel.viewState.test {
            assertTrue(awaitItem() is CatBreedListViewState.Loading)
        }
    }

    @Test
    fun `Given the cat breed list was requested When the interactor emits an error state Then emit an error view state`() = runTest {
        val interactor = FakeBreedListInteractor(
            currentState = BreedListState.Error
        )

        val viewModel = CatBreedListViewModel(
            interactor
        )

        viewModel.viewState.test {
            assertTrue(awaitItem() is CatBreedListViewState.Error)
        }
    }

    @Test
    fun `Given the cat breed list was requested When the interactor emits a loaded state Then emit an loaded view state`() = runTest {
        val interactor = FakeBreedListInteractor(
            currentState = BreedListState.Loaded(
                listOf(
                    CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("temperament"), false),
                    CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("temperament"), false)
                )
            )
        )

        val expectedViewState = CatBreedListViewState.Loaded(
            listOf(
                CatBreedViewItem("image1", "name1"), CatBreedViewItem("image2", "name2")
            )
        )

        val viewModel = CatBreedListViewModel(
            interactor
        )

        viewModel.viewState.test {
            assertEquals(expectedViewState, awaitItem())
        }
    }
}