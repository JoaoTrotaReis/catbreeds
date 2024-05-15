package com.joaoreis.catbreeds.catbreedlist.domain

import app.cash.turbine.test
import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.FakeBreedRepository
import com.joaoreis.catbreeds.favorites.data.FakeFavoriteCatBreedsGateway
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BreedListInteractorTests {

    @Test
    fun `Given there are available cat breeds When cat breed list is requested Then emit a Loading state followed by a Loaded state with the breed list`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val catBreeds = listOf(
            CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("temperament"), false),
            CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("temperament"), false)
        )

        val breedRepository: BreedRepository = FakeBreedRepository(Result.Success(catBreeds))

        val interactor = BreedListInteractorImplementation(
            breedRepository = breedRepository,
            favoriteCatBreedsGateway = FakeFavoriteCatBreedsGateway(),
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
            favoriteCatBreedsGateway = FakeFavoriteCatBreedsGateway(),
            dispatcher = dispatcher
        )

        interactor.state.test {
            awaitItem()
            interactor.loadCatBreedList()
            assertTrue(awaitItem() is BreedListState.Loading)
            assertTrue(awaitItem() is BreedListState.Error)
        }
    }

    @Test
    fun `Given there are cat breeds for a search term When cat breeds are searched by that term Then emit a Loading state followed by a SearchLoaded state with the cat breeds`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val catBreeds = listOf(
            CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("temperament"), false),
            CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("temperament"), false)
        )

        val breedRepository: BreedRepository = FakeBreedRepository(searchResponse = Result.Success(catBreeds))

        val interactor = BreedListInteractorImplementation(
            breedRepository = breedRepository,
            favoriteCatBreedsGateway = FakeFavoriteCatBreedsGateway(),
            dispatcher = dispatcher
        )

        interactor.state.test {
            awaitItem()
            interactor.searchCatBreeds("searchTerm")
            assertTrue(awaitItem() is BreedListState.Loading)
            assertEquals(BreedListState.SearchLoaded(catBreeds), awaitItem())
        }
    }

    @Test
    fun `Given a search When cat breeds are searched by that term And there is an error Then emit a Loading state followed by a SearchError state`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)

        val breedRepository: BreedRepository = FakeBreedRepository(searchResponse = Result.Error())

        val interactor = BreedListInteractorImplementation(
            breedRepository = breedRepository,
            favoriteCatBreedsGateway = FakeFavoriteCatBreedsGateway(),
            dispatcher = dispatcher
        )

        interactor.state.test {
            awaitItem()
            interactor.searchCatBreeds("name")
            assertTrue(awaitItem() is BreedListState.Loading)
            assertTrue(awaitItem() is BreedListState.SearchError)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given a Loaded state with a list of cat breeds When a favorite update is received Then update the favorite in the cat breed list And emit a Loaded state with the updated list`() = runTest {
        val catBreeds = listOf(
            CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("temperament"), false),
            CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("temperament"), false)
        )

        val updatedCatBreeds = listOf(
            CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("temperament"), true),
            CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("temperament"), false)
        )

        val flow = MutableSharedFlow<FavoriteUpdate>()
        val favoritesGateway = FakeFavoriteCatBreedsGateway(favoriteUdpdatesFlow =flow)

        val interactor = BreedListInteractorImplementation(
            breedRepository = FakeBreedRepository(),
            favoriteCatBreedsGateway = favoritesGateway,
            dispatcher = UnconfinedTestDispatcher(),
            initialState = BreedListState.Loaded(catBreeds)
        )

        interactor.state.test {
            awaitItem()
            flow.emit(FavoriteUpdate("id1", true))
            assertEquals(BreedListState.Loaded(updatedCatBreeds), awaitItem())
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given a SearchLoaded state with a list of cat breeds When a favorite update is received Then update the favorite in the cat breed list And emit a SearchLoaded state with the updated list`() = runTest {
        val catBreeds = listOf(
            CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("temperament"), false),
            CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("temperament"), false)
        )

        val updatedCatBreeds = listOf(
            CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("temperament"), true),
            CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("temperament"), false)
        )

        val flow = MutableSharedFlow<FavoriteUpdate>()
        val favoritesGateway = FakeFavoriteCatBreedsGateway(favoriteUdpdatesFlow =flow)

        val interactor = BreedListInteractorImplementation(
            breedRepository = FakeBreedRepository(),
            favoriteCatBreedsGateway = favoritesGateway,
            dispatcher = UnconfinedTestDispatcher(),
            initialState = BreedListState.SearchLoaded(catBreeds)
        )

        interactor.state.test {
            awaitItem()
            flow.emit(FavoriteUpdate("id1", true))
            assertEquals(BreedListState.SearchLoaded(updatedCatBreeds), awaitItem())
        }
    }
}