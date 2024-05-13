package com.joaoreis.catbreeds.favorites

import app.cash.turbine.test
import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedEntity
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsGateway
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsInteractorImplementation
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsState
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FavoriteCatBreedsInteractorTests {
    @Test
    fun `Given there are favorite cat breeds stored When favorite cat breeds are requested Then emit a Loading state followed by a Loaded with the favorite cat breeds`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val catBreeds = listOf(
            CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("one","two"), false),
            CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("three","four"), false)
        )

        val expectedResult = FavoriteCatBreedsState.Loaded(catBreeds)

        val interactor = FavoriteCatBreedsInteractorImplementation(
            favoriteCatBreedsGateway = FakeFavoriteCatBreedsGateway(Result.Success(catBreeds)),
            dispatcher = dispatcher
        )

        interactor.state.test {
            awaitItem()
            interactor.loadFavoriteCatBreeds()
            assertTrue(awaitItem() is FavoriteCatBreedsState.Loading)
            assertEquals(expectedResult, awaitItem())
        }
    }

    @Test
    fun `Given there is an error loading stored cat breeds When favorite cat breeds are requested Then emit a Loading state followed by an Error state`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)

        val interactor = FavoriteCatBreedsInteractorImplementation(
            favoriteCatBreedsGateway = FakeFavoriteCatBreedsGateway(Result.Error()),
            dispatcher = dispatcher
        )

        interactor.state.test {
            awaitItem()
            interactor.loadFavoriteCatBreeds()
            assertTrue(awaitItem() is FavoriteCatBreedsState.Loading)
            assertTrue(awaitItem() is FavoriteCatBreedsState.Error)
        }
    }
}