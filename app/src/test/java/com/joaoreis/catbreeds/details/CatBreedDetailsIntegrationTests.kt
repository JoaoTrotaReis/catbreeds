package com.joaoreis.catbreeds.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedEntity
import com.joaoreis.catbreeds.catbreedlist.data.local.FakeCatBreedsDao
import com.joaoreis.catbreeds.catbreedlist.data.remote.CatBreedDTO
import com.joaoreis.catbreeds.catbreedlist.data.remote.FakeCatApi
import com.joaoreis.catbreeds.catbreedlist.data.remote.Image
import com.joaoreis.catbreeds.catbreedlist.domain.BreedListInteractorImplementation
import com.joaoreis.catbreeds.details.data.BreedDetailsLocalSourceImplementation
import com.joaoreis.catbreeds.details.data.CatBreedDetailsRemoteSourceImplementation
import com.joaoreis.catbreeds.details.data.CatBreedDetailsRepositoryImplementation
import com.joaoreis.catbreeds.details.domain.CatBreedDetailsInteractorImplementation
import com.joaoreis.catbreeds.details.domain.CatBreedDetailsState
import com.joaoreis.catbreeds.details.presentation.CatBreedDetails
import com.joaoreis.catbreeds.details.presentation.CatBreedDetailsViewModel
import com.joaoreis.catbreeds.details.presentation.CatBreedDetailsViewState
import com.joaoreis.catbreeds.favorites.data.FavoriteCatBreedsGatewayImplementation
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsInteractorImplementation
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

class CatBreedDetailsIntegrationTests {
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
    fun `Given a cat breed details are available locally When details are requested Then emit a Loaded view state with cat breed details`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val catBreedsDao = FakeCatBreedsDao(
            findResult = CatBreedEntity("id1", "description1", "origin1", "name1", "image1", listOf("one","two"), false)
        )

        val expectedState = CatBreedDetailsViewState.Loaded(
            CatBreedDetails("id1","image1","name1", "description1", "origin1", listOf("one","two"), false)
        )

        val catApi = FakeCatApi()

        val breedDetailsLocalSource = BreedDetailsLocalSourceImplementation(catBreedsDao)
        val breedDetailsRemoteSource = CatBreedDetailsRemoteSourceImplementation(catApi)

        val breedDetailsRepository = CatBreedDetailsRepositoryImplementation(
            breedDetailsLocalSource, breedDetailsRemoteSource, dispatcher
        )

        val favoriteCatBreedsGateway = FavoriteCatBreedsGatewayImplementation(catBreedsDao, dispatcher)

        val favoriteCatBreedsInteractor = FavoriteCatBreedsInteractorImplementation(
            favoriteCatBreedsGateway, dispatcher
        )

        val breedDetailsInteractor = CatBreedDetailsInteractorImplementation(
            breedDetailsRepository, dispatcher
        )

        val viewModel = CatBreedDetailsViewModel(
            savedStateHandle = SavedStateHandle().apply { set("id", "id1") },
            catBreedDetailsInteractor = breedDetailsInteractor,
            favoriteCatBreedsInteractor = favoriteCatBreedsInteractor
        )

        viewModel.viewState.test {
            assertTrue(awaitItem() is CatBreedDetailsViewState.Loading)
            viewModel.loadDetails()
            assertEquals(expectedState, awaitItem())
        }
    }

    @Test
    fun `Given a cat breed details are available remotely And not locally When details are requested Then emit a Loaded view state with cat breed details`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val catBreedsDao = FakeCatBreedsDao()

        val expectedState = CatBreedDetailsViewState.Loaded(
            CatBreedDetails("id1","image1","name1", "description1", "origin1", listOf("one","two"), false)
        )

        val catApi = FakeCatApi(
            findResult = CatBreedDTO("id1", "one, two", "description1", "origin1", "name1", "id", Image("image1")),
        )

        val breedDetailsLocalSource = BreedDetailsLocalSourceImplementation(catBreedsDao)
        val breedDetailsRemoteSource = CatBreedDetailsRemoteSourceImplementation(catApi)

        val breedDetailsRepository = CatBreedDetailsRepositoryImplementation(
            breedDetailsLocalSource, breedDetailsRemoteSource, dispatcher
        )

        val favoriteCatBreedsGateway = FavoriteCatBreedsGatewayImplementation(catBreedsDao, dispatcher)

        val favoriteCatBreedsInteractor = FavoriteCatBreedsInteractorImplementation(
            favoriteCatBreedsGateway, dispatcher
        )

        val breedDetailsInteractor = CatBreedDetailsInteractorImplementation(
            breedDetailsRepository, dispatcher
        )

        val viewModel = CatBreedDetailsViewModel(
            savedStateHandle = SavedStateHandle().apply { set("id", "id1") },
            catBreedDetailsInteractor = breedDetailsInteractor,
            favoriteCatBreedsInteractor = favoriteCatBreedsInteractor
        )

        viewModel.viewState.test {
            assertTrue(awaitItem() is CatBreedDetailsViewState.Loading)
            viewModel.loadDetails()
            assertEquals(expectedState, awaitItem())
        }
    }

    @Test
    fun `Given there is an error getting breed details remotely and locally When details are requested Then emit an Error view state`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val catBreedsDao = FakeCatBreedsDao()

        val catApi = FakeCatApi()

        val breedDetailsLocalSource = BreedDetailsLocalSourceImplementation(catBreedsDao)
        val breedDetailsRemoteSource = CatBreedDetailsRemoteSourceImplementation(catApi)

        val breedDetailsRepository = CatBreedDetailsRepositoryImplementation(
            breedDetailsLocalSource, breedDetailsRemoteSource, dispatcher
        )

        val favoriteCatBreedsGateway = FavoriteCatBreedsGatewayImplementation(catBreedsDao, dispatcher)

        val favoriteCatBreedsInteractor = FavoriteCatBreedsInteractorImplementation(
            favoriteCatBreedsGateway, dispatcher
        )

        val breedDetailsInteractor = CatBreedDetailsInteractorImplementation(
            breedDetailsRepository, dispatcher
        )

        val viewModel = CatBreedDetailsViewModel(
            savedStateHandle = SavedStateHandle().apply { set("id", "id1") },
            catBreedDetailsInteractor = breedDetailsInteractor,
            favoriteCatBreedsInteractor = favoriteCatBreedsInteractor
        )

        viewModel.viewState.test {
            assertTrue(awaitItem() is CatBreedDetailsViewState.Loading)
            viewModel.loadDetails()
            assertTrue(awaitItem() is CatBreedDetailsViewState.Error)
        }
    }

    @Test
    fun `Given cat breed details are loaded When favorite is toggled Then emit a view state with correct favorite value`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val catBreedsDao = FakeCatBreedsDao(
            findResult = CatBreedEntity("id1", "description1", "origin1", "name1", "image1", listOf("one","two"), false)
        )

        val expectedState = CatBreedDetailsViewState.Loaded(
            CatBreedDetails("id1","image1","name1", "description1", "origin1", listOf("one","two"), true)
        )

        val catApi = FakeCatApi()

        val breedDetailsLocalSource = BreedDetailsLocalSourceImplementation(catBreedsDao)
        val breedDetailsRemoteSource = CatBreedDetailsRemoteSourceImplementation(catApi)

        val breedDetailsRepository = CatBreedDetailsRepositoryImplementation(
            breedDetailsLocalSource, breedDetailsRemoteSource, dispatcher
        )

        val favoriteCatBreedsGateway = FavoriteCatBreedsGatewayImplementation(catBreedsDao, dispatcher)

        val favoriteCatBreedsInteractor = FavoriteCatBreedsInteractorImplementation(
            favoriteCatBreedsGateway, dispatcher
        )

        val breedDetailsInteractor = CatBreedDetailsInteractorImplementation(
            breedDetailsRepository, dispatcher
        )

        val viewModel = CatBreedDetailsViewModel(
            savedStateHandle = SavedStateHandle().apply { set("id", "id1") },
            catBreedDetailsInteractor = breedDetailsInteractor,
            favoriteCatBreedsInteractor = favoriteCatBreedsInteractor
        )

        viewModel.viewState.test {
            assertTrue(awaitItem() is CatBreedDetailsViewState.Loading)
            viewModel.loadDetails()
            awaitItem()
            viewModel.onFavoriteToggle("id1", true)
            assertEquals(expectedState, awaitItem())
        }
    }
}