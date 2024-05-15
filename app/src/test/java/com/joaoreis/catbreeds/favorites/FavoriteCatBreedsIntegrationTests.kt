package com.joaoreis.catbreeds.favorites

import app.cash.turbine.test
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedEntity
import com.joaoreis.catbreeds.catbreedlist.data.local.FakeCatBreedsDao
import com.joaoreis.catbreeds.catbreedlist.presentation.CatBreedListViewState
import com.joaoreis.catbreeds.catbreedlist.presentation.CatBreedViewItem
import com.joaoreis.catbreeds.favorites.data.FavoriteCatBreedsGatewayImplementation
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsInteractorImplementation
import com.joaoreis.catbreeds.favorites.presentation.FavoriteCatBreedViewItem
import com.joaoreis.catbreeds.favorites.presentation.FavoriteCatBreedsViewModel
import com.joaoreis.catbreeds.favorites.presentation.FavoriteCatBreedsViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FavoriteCatBreedsIntegrationTests {
    @OptIn(ExperimentalCoroutinesApi::class)
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
    fun `Given there are favorites When favorites are requested Then emit a loaded view state with favorite list`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val favoriteEntities = listOf(
            CatBreedEntity("id1", "description1", "origin1", "name1", "image1", listOf("one","two"), false),
            CatBreedEntity("id2", "description2", "origin2", "name2", "image2", listOf("three","four"), false)
        )

        val expectedState = FavoriteCatBreedsViewState.Loaded(
            listOf(
                FavoriteCatBreedViewItem("id1", "image1", "name1"),
                FavoriteCatBreedViewItem("id2", "image2", "name2"),
            )
        )

        val catBreedsDao = FakeCatBreedsDao(favoritesResult = favoriteEntities)
        val favoriteCatBreedsGateway = FavoriteCatBreedsGatewayImplementation(catBreedsDao, dispatcher)

        val favoriteCatBreedsInteractor = FavoriteCatBreedsInteractorImplementation(favoriteCatBreedsGateway, dispatcher)

        val viewModel = FavoriteCatBreedsViewModel(
            favoriteCatBreedsInteractor
        )

        viewModel.viewState.test {
            assertTrue(awaitItem() is FavoriteCatBreedsViewState.Loading)
            assertEquals(expectedState, awaitItem())
        }
    }

    @Test
    fun `Given there is an error getting favorites When favorites are requested Then emit an error view state `() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)

        val catBreedsDao = FakeCatBreedsDao()
        val favoriteCatBreedsGateway = FavoriteCatBreedsGatewayImplementation(catBreedsDao, dispatcher)

        val favoriteCatBreedsInteractor = FavoriteCatBreedsInteractorImplementation(favoriteCatBreedsGateway, dispatcher)

        val viewModel = FavoriteCatBreedsViewModel(
            favoriteCatBreedsInteractor
        )

        viewModel.viewState.test {
            assertTrue(awaitItem() is FavoriteCatBreedsViewState.Loading)
            assertTrue(awaitItem() is FavoriteCatBreedsViewState.Error)
        }
    }

    @Test
    fun `Given favorites are loaded When breed is removed from favorites Then emit a loaded view state reloaded favorite list`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val favoriteEntities = listOf(
            CatBreedEntity("id1", "description1", "origin1", "name1", "image1", listOf("one","two"), false),
            CatBreedEntity("id2", "description2", "origin2", "name2", "image2", listOf("three","four"), false)
        )

        val expectedState = FavoriteCatBreedsViewState.Loaded(
            listOf(
                FavoriteCatBreedViewItem("id1", "image1", "name1"),
                FavoriteCatBreedViewItem("id2", "image2", "name2"),
            )
        )

        val catBreedsDao = FakeCatBreedsDao(favoritesResult = favoriteEntities)
        val favoriteCatBreedsGateway = FavoriteCatBreedsGatewayImplementation(catBreedsDao, dispatcher)

        val favoriteCatBreedsInteractor = FavoriteCatBreedsInteractorImplementation(favoriteCatBreedsGateway, dispatcher)

        val viewModel = FavoriteCatBreedsViewModel(
            favoriteCatBreedsInteractor
        )

        viewModel.viewState.test {
            assertTrue(awaitItem() is FavoriteCatBreedsViewState.Loading)
            assertTrue(awaitItem() is FavoriteCatBreedsViewState.Loaded)
            viewModel.removeFromFavorites("id1")
            assertTrue(awaitItem() is FavoriteCatBreedsViewState.Loading)
            assertEquals(expectedState, awaitItem())
        }
    }
}