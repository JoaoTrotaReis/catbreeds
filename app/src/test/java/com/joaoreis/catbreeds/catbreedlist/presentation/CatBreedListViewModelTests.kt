package com.joaoreis.catbreeds.catbreedlist.presentation

import app.cash.turbine.test
import com.joaoreis.catbreeds.catbreedlist.domain.BreedListState
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsState
import com.joaoreis.catbreeds.favorites.presentation.FakeFavoriteCatBreedsInteractor
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
        val breedListInteractor = FakeBreedListInteractor(
            currentState = BreedListState.Loading
        )
        val favoriteListInteractor = FakeFavoriteCatBreedsInteractor(
            currentState = FavoriteCatBreedsState.Idle
        )

        val viewModel = CatBreedListViewModel(
            breedListInteractor, favoriteListInteractor
        )

        viewModel.viewState.test {
            assertTrue(awaitItem() is CatBreedListViewState.Loading)
        }
    }

    @Test
    fun `Given the cat breed list was requested When the interactor emits an error state Then emit an error view state`() = runTest {
        val breedListInteractor = FakeBreedListInteractor(
            currentState = BreedListState.Error
        )

        val favoriteListInteractor = FakeFavoriteCatBreedsInteractor(
            currentState = FavoriteCatBreedsState.Idle
        )

        val viewModel = CatBreedListViewModel(
            breedListInteractor, favoriteListInteractor
        )

        viewModel.viewState.test {
            assertTrue(awaitItem() is CatBreedListViewState.Error)
        }
    }

    @Test
    fun `Given the cat breed list was requested When the interactor emits a loaded state Then emit an loaded view state`() = runTest {
        val breedListInteractor = FakeBreedListInteractor(
            currentState = BreedListState.Loaded(
                listOf(
                    CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("temperament"), true),
                    CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("temperament"), true)
                )
            )
        )

        val favoriteListInteractor = FakeFavoriteCatBreedsInteractor(
            currentState = FavoriteCatBreedsState.Idle
        )

        val expectedViewState = CatBreedListViewState.Loaded(
            listOf(
                CatBreedViewItem("id1","image1", "name1", true),
                CatBreedViewItem("id2","image2", "name2", true)
            )
        )

        val viewModel = CatBreedListViewModel(
            breedListInteractor, favoriteListInteractor
        )

        viewModel.viewState.test {
            assertEquals(expectedViewState, awaitItem())
        }
    }

    @Test
    fun `Given the cat breed list is loaded When a cat breed is added to favorites Then emit a new loaded view state with that cat breed as favorite`() = runTest {
        val breedListInteractor = FakeBreedListInteractor(
            currentState = BreedListState.Loaded(
                listOf(
                    CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("temperament"), false),
                    CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("temperament"), false)
                )
            )
        )

        val favoriteListInteractor = FakeFavoriteCatBreedsInteractor(
            currentState = FavoriteCatBreedsState.Idle
        )

        val expectedViewState = CatBreedListViewState.Loaded(
            listOf(
                CatBreedViewItem("id1","image1", "name1", true),
                CatBreedViewItem("id2","image2", "name2", false)
            )
        )

        val viewModel = CatBreedListViewModel(
            breedListInteractor, favoriteListInteractor
        )

        viewModel.viewState.test {
            awaitItem()
            viewModel.toggleFavorite("id1", true)
            assertEquals(expectedViewState, awaitItem())
        }
    }

    @Test
    fun `Given the cat breed list is loaded When a cat breed is removed from the favorites Then emit a new loaded view state with that cat breed as not favorite`() = runTest {
        val breedListInteractor = FakeBreedListInteractor(
            currentState = BreedListState.Loaded(
                listOf(
                    CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("temperament"), true),
                    CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("temperament"), false)
                )
            )
        )

        val favoriteListInteractor = FakeFavoriteCatBreedsInteractor(
            currentState = FavoriteCatBreedsState.Idle
        )

        val expectedViewState = CatBreedListViewState.Loaded(
            listOf(
                CatBreedViewItem("id1","image1", "name1", false),
                CatBreedViewItem("id2","image2", "name2", false)
            )
        )

        val viewModel = CatBreedListViewModel(
            breedListInteractor, favoriteListInteractor
        )

        viewModel.viewState.test {
            awaitItem()
            viewModel.toggleFavorite("id1", false)
            assertEquals(expectedViewState, awaitItem())
        }
    }
}