package com.joaoreis.catbreeds.details.domain

import app.cash.turbine.test
import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class CatBreedDetailsInteractorTests {
    @Test
    fun `Given cat breed details are available When cat breed details are requested Then emit a Loading state followed by a Loaded state with the cat breed details`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val catBreed = CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("temperament"), false)

        val detailsRepository = FakeCatBreedDetailsRepository(Result.Success(catBreed))

        val interactor = CatBreedDetailsInteractorImplementation(
            catBreedDetailsRepository = detailsRepository,
            dispatcher = dispatcher
        )

        interactor.state.test {
            awaitItem()
            interactor.loadCatBreedDetails("id")
            Assert.assertTrue(awaitItem() is CatBreedDetailsState.Loading)
            Assert.assertEquals(CatBreedDetailsState.Loaded(catBreed), awaitItem())
        }
    }

    @Test
    fun `Given there is an problem loading cat breed details When cat breed details are requested Then emit a Loading state followed by an Error state`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)

        val detailsRepository = FakeCatBreedDetailsRepository(Result.Error())

        val interactor = CatBreedDetailsInteractorImplementation(
            catBreedDetailsRepository = detailsRepository,
            dispatcher = dispatcher
        )


        interactor.state.test {
            awaitItem()
            interactor.loadCatBreedDetails("id")
            Assert.assertTrue(awaitItem() is CatBreedDetailsState.Loading)
            Assert.assertTrue(awaitItem() is CatBreedDetailsState.Error)
        }
    }
}