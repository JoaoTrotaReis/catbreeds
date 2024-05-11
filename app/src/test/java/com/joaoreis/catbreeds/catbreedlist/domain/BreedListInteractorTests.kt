package com.joaoreis.catbreeds.catbreedlist.domain

import app.cash.turbine.test
import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.FakeBreedRepository
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BreedListInteractorTests {

    @Test
    fun `Given there are available cat breeds When cat breed list is requested Then emit a Loading state followed by a Loaded state with the breed list`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val catBreeds = listOf(
            CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("temperament")),
            CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("temperament"))
        )

        val breedRepository: BreedRepository = FakeBreedRepository(Result.Success(catBreeds))

        val interactor = BreedListInteractorImplementation(
            breedRepository = breedRepository,
            dispatcher = dispatcher
        )

        interactor.state.test {
            awaitItem()
            interactor.loadCatBreedList()
            assertTrue(awaitItem() is BreedListState.Loading)
            assertEquals(BreedListState.Loaded(catBreeds), awaitItem())
        }
    }

    @Test
    fun `Given there is an problem loading cat breeds When cat breed list is requested Then emit a Loading state followed by an Error state`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)


        val breedRepository: BreedRepository = FakeBreedRepository(Result.Error())

        val interactor = BreedListInteractorImplementation(
            breedRepository = breedRepository,
            dispatcher = dispatcher
        )

        interactor.state.test {
            awaitItem()
            interactor.loadCatBreedList()
            assertTrue(awaitItem() is BreedListState.Loading)
            assertTrue(awaitItem() is BreedListState.Error)
        }
    }
}