package com.joaoreis.catbreeds.catbreedlist

import app.cash.turbine.test
import com.joaoreis.catbreeds.catbreedlist.data.BreedRepositoryImplementation
import com.joaoreis.catbreeds.catbreedlist.data.local.BreedLocalDataSourceImplementation
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedEntity
import com.joaoreis.catbreeds.catbreedlist.data.local.FakeCatBreedsDao
import com.joaoreis.catbreeds.catbreedlist.data.remote.BreedRemoteDataSourceImplementation
import com.joaoreis.catbreeds.catbreedlist.data.remote.CatBreedDTO
import com.joaoreis.catbreeds.catbreedlist.data.remote.FakeCatApi
import com.joaoreis.catbreeds.catbreedlist.data.remote.Image
import com.joaoreis.catbreeds.catbreedlist.domain.BreedListInteractorImplementation
import com.joaoreis.catbreeds.catbreedlist.domain.BreedListState
import com.joaoreis.catbreeds.catbreedlist.presentation.CatBreedListViewModel
import com.joaoreis.catbreeds.catbreedlist.presentation.CatBreedListViewState
import com.joaoreis.catbreeds.catbreedlist.presentation.CatBreedViewItem
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

class CatBreedListIntegrationTests {

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
    fun `Given there are cat breeds in local storage When cat breeds are loaded Then emit a loaded view state with local cat breeds`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val catBreedEntities = listOf(
            CatBreedEntity("id1", "description1", "origin1", "name1", "image1", listOf("one","two"), false),
            CatBreedEntity("id2", "description2", "origin2", "name2", "image2", listOf("three","four"), false)
        )

        val expectedState = CatBreedListViewState.Loaded(
            listOf(
                CatBreedViewItem("id1", "image1", "name1", false),
                CatBreedViewItem("id2", "image2", "name2", false),
            )
        )

        val catBreedsDao = FakeCatBreedsDao(
            getCatBreedsResult = catBreedEntities
        )
        val catApi = FakeCatApi()

        val breedListRepository = BreedRepositoryImplementation(
            BreedLocalDataSourceImplementation(catBreedsDao),
            BreedRemoteDataSourceImplementation(catApi),
            dispatcher
        )

        val favoriteCatBreedsGateway = FavoriteCatBreedsGatewayImplementation(catBreedsDao, dispatcher)

        val breedListInteractor = BreedListInteractorImplementation(
            breedListRepository, favoriteCatBreedsGateway, dispatcher
        )

        val favoriteCatBreedsInteractor = FavoriteCatBreedsInteractorImplementation(
            favoriteCatBreedsGateway, dispatcher
        )

        val viewModel = CatBreedListViewModel(
            breedListInteractor = breedListInteractor,
            favoriteCatBreedsInteractor = favoriteCatBreedsInteractor
        )

        viewModel.viewState.test {
            assertTrue(awaitItem() is CatBreedListViewState.Loading)
            assertEquals(expectedState, awaitItem())
        }
    }

    @Test
    fun `Given there are no cat breeds in local storage And there are breeds in remote data source When cat breeds are loaded Then emit a loaded view state with remote cat breeds`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val catBreedDTOs = listOf(
            CatBreedDTO("id1", "one, two", "description1", "origin1", "name1", "id", Image("image1")),
            CatBreedDTO("id2", "three, four", "description2", "origin2", "name2","id", Image("image2"))
        )

        val expectedState = CatBreedListViewState.Loaded(
            listOf(
                CatBreedViewItem("id1", "image1", "name1", false),
                CatBreedViewItem("id2", "image2", "name2", false),
            )
        )

        val catBreedsDao = FakeCatBreedsDao()
        val catApi = FakeCatApi(catBreedDTOs)

        val breedListRepository = BreedRepositoryImplementation(
            BreedLocalDataSourceImplementation(catBreedsDao),
            BreedRemoteDataSourceImplementation(catApi),
            dispatcher
        )

        val favoriteCatBreedsGateway = FavoriteCatBreedsGatewayImplementation(catBreedsDao, dispatcher)

        val breedListInteractor = BreedListInteractorImplementation(
            breedListRepository, favoriteCatBreedsGateway, dispatcher
        )

        val favoriteCatBreedsInteractor = FavoriteCatBreedsInteractorImplementation(
            favoriteCatBreedsGateway, dispatcher
        )

        val viewModel = CatBreedListViewModel(
            breedListInteractor = breedListInteractor,
            favoriteCatBreedsInteractor = favoriteCatBreedsInteractor
        )

        viewModel.viewState.test {
            assertTrue(awaitItem() is CatBreedListViewState.Loading)
            assertEquals(expectedState, awaitItem())
        }
    }

    @Test
    fun `Given there is an error getting cat breeds from local storage And remote data source When cat breeds are loaded Then emit an error view state`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)

        val catBreedsDao = FakeCatBreedsDao()
        val catApi = FakeCatApi()

        val breedListRepository = BreedRepositoryImplementation(
            BreedLocalDataSourceImplementation(catBreedsDao),
            BreedRemoteDataSourceImplementation(catApi),
            dispatcher
        )

        val favoriteCatBreedsGateway = FavoriteCatBreedsGatewayImplementation(catBreedsDao, dispatcher)

        val breedListInteractor = BreedListInteractorImplementation(
            breedListRepository, favoriteCatBreedsGateway, dispatcher
        )

        val favoriteCatBreedsInteractor = FavoriteCatBreedsInteractorImplementation(
            favoriteCatBreedsGateway, dispatcher
        )

        val viewModel = CatBreedListViewModel(
            breedListInteractor = breedListInteractor,
            favoriteCatBreedsInteractor = favoriteCatBreedsInteractor
        )

        viewModel.viewState.test {
            assertTrue(awaitItem() is CatBreedListViewState.Loading)
            assertTrue(awaitItem() is CatBreedListViewState.Error)
        }
    }

    @Test
    fun `Given a list of cat breeds When a cat breed is added to the favorites Then emit a loaded view state with cat breed as favorite`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val catBreedEntities = listOf(
            CatBreedEntity("id1", "description1", "origin1", "name1", "image1", listOf("one","two"), false),
            CatBreedEntity("id2", "description2", "origin2", "name2", "image2", listOf("three","four"), false)
        )

        val expectedState = CatBreedListViewState.Loaded(
            listOf(
                CatBreedViewItem("id1", "image1", "name1", true),
                CatBreedViewItem("id2", "image2", "name2", false),
            )
        )

        val catBreedsDao = FakeCatBreedsDao(
            getCatBreedsResult = catBreedEntities
        )
        val catApi = FakeCatApi()

        val breedListRepository = BreedRepositoryImplementation(
            BreedLocalDataSourceImplementation(catBreedsDao),
            BreedRemoteDataSourceImplementation(catApi),
            dispatcher
        )

        val favoriteCatBreedsGateway = FavoriteCatBreedsGatewayImplementation(catBreedsDao, dispatcher)

        val breedListInteractor = BreedListInteractorImplementation(
            breedListRepository, favoriteCatBreedsGateway, dispatcher
        )

        val favoriteCatBreedsInteractor = FavoriteCatBreedsInteractorImplementation(
            favoriteCatBreedsGateway, dispatcher
        )

        val viewModel = CatBreedListViewModel(
            breedListInteractor = breedListInteractor,
            favoriteCatBreedsInteractor = favoriteCatBreedsInteractor
        )

        viewModel.viewState.test {
            awaitItem()
            awaitItem()
            viewModel.toggleFavorite("id1", true)
            assertEquals(expectedState, awaitItem())
        }
    }

    @Test
    fun `Given there are local results for search query When cat breeds are searched Then emit a loaded view state with searched local cat breeds`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val catBreedEntities = listOf(
            CatBreedEntity("id1", "description1", "origin1", "name1", "image1", listOf("one","two"), false),
            CatBreedEntity("id2", "description2", "origin2", "name2", "image2", listOf("three","four"), false)
        )

        val catBreedSearchEntities = listOf(
            CatBreedEntity("id3", "description1", "origin1", "name1", "image1", listOf("one","two"), false),
            CatBreedEntity("id4", "description2", "origin2", "name2", "image2", listOf("three","four"), false)
        )

        val expectedState = CatBreedListViewState.Loaded(
            listOf(
                CatBreedViewItem("id3", "image1", "name1", false),
                CatBreedViewItem("id4", "image2", "name2", false),
            )
        )

        val catBreedsDao = FakeCatBreedsDao(
            searchResult = catBreedSearchEntities, getCatBreedsResult = catBreedEntities
        )
        val catApi = FakeCatApi()

        val breedListRepository = BreedRepositoryImplementation(
            BreedLocalDataSourceImplementation(catBreedsDao),
            BreedRemoteDataSourceImplementation(catApi),
            dispatcher
        )

        val favoriteCatBreedsGateway = FavoriteCatBreedsGatewayImplementation(catBreedsDao, dispatcher)

        val breedListInteractor = BreedListInteractorImplementation(
            breedListRepository, favoriteCatBreedsGateway, dispatcher
        )

        val favoriteCatBreedsInteractor = FavoriteCatBreedsInteractorImplementation(
            favoriteCatBreedsGateway, dispatcher
        )

        val viewModel = CatBreedListViewModel(
            breedListInteractor = breedListInteractor,
            favoriteCatBreedsInteractor = favoriteCatBreedsInteractor
        )

        viewModel.viewState.test {
            assertTrue(awaitItem() is CatBreedListViewState.Loading)
            assertTrue(awaitItem() is CatBreedListViewState.Loaded)
            viewModel.searchCatBreeds("name")
            assertTrue(awaitItem() is CatBreedListViewState.Loading)
            assertEquals(expectedState, awaitItem())
        }
    }

    @Test
    fun `Given there are local no results for search query And there are remote results When cat breeds are searched Then emit a loaded view state with searched remote cat breeds`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val catBreedDTOs = listOf(
            CatBreedDTO("id1", "one, two", "description1", "origin1", "name1", "id", Image("image1")),
            CatBreedDTO("id2", "three, four", "description2", "origin2", "name2","id", Image("image2"))
        )

        val catBreedSearchDTOs = listOf(
            CatBreedDTO("id3", "one, two", "description1", "origin1", "name1", "id", Image("image1")),
            CatBreedDTO("id4", "three, four", "description2", "origin2", "name2","id", Image("image2"))
        )

        val expectedState = CatBreedListViewState.Loaded(
            listOf(
                CatBreedViewItem("id3", "image1", "name1", false),
                CatBreedViewItem("id4", "image2", "name2", false),
            )
        )

        val catBreedsDao = FakeCatBreedsDao()
        val catApi = FakeCatApi(catBreedList = catBreedDTOs, searchResults = catBreedSearchDTOs)

        val breedListRepository = BreedRepositoryImplementation(
            BreedLocalDataSourceImplementation(catBreedsDao),
            BreedRemoteDataSourceImplementation(catApi),
            dispatcher
        )

        val favoriteCatBreedsGateway = FavoriteCatBreedsGatewayImplementation(catBreedsDao, dispatcher)

        val breedListInteractor = BreedListInteractorImplementation(
            breedListRepository, favoriteCatBreedsGateway, dispatcher
        )

        val favoriteCatBreedsInteractor = FavoriteCatBreedsInteractorImplementation(
            favoriteCatBreedsGateway, dispatcher
        )

        val viewModel = CatBreedListViewModel(
            breedListInteractor = breedListInteractor,
            favoriteCatBreedsInteractor = favoriteCatBreedsInteractor
        )

        viewModel.viewState.test {
            assertTrue(awaitItem() is CatBreedListViewState.Loading)
            assertTrue(awaitItem() is CatBreedListViewState.Loaded)
            viewModel.searchCatBreeds("name")
            assertTrue(awaitItem() is CatBreedListViewState.Loading)
            assertEquals(expectedState, awaitItem())
        }
    }

    @Test
    fun `Given there are no local and remote results When cat breeds are searched Then emit a empty serach view state`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val catBreedDTOs = listOf(
            CatBreedDTO("id1", "one, two", "description1", "origin1", "name1", "id", Image("image1")),
            CatBreedDTO("id2", "three, four", "description2", "origin2", "name2","id", Image("image2"))
        )


        val catBreedsDao = FakeCatBreedsDao(searchResult = listOf())
        val catApi = FakeCatApi(catBreedList = catBreedDTOs, searchResults = listOf())

        val breedListRepository = BreedRepositoryImplementation(
            BreedLocalDataSourceImplementation(catBreedsDao),
            BreedRemoteDataSourceImplementation(catApi),
            dispatcher
        )

        val favoriteCatBreedsGateway = FavoriteCatBreedsGatewayImplementation(catBreedsDao, dispatcher)

        val breedListInteractor = BreedListInteractorImplementation(
            breedListRepository, favoriteCatBreedsGateway, dispatcher
        )

        val favoriteCatBreedsInteractor = FavoriteCatBreedsInteractorImplementation(
            favoriteCatBreedsGateway, dispatcher
        )

        val viewModel = CatBreedListViewModel(
            breedListInteractor = breedListInteractor,
            favoriteCatBreedsInteractor = favoriteCatBreedsInteractor
        )

        viewModel.viewState.test {
            assertTrue(awaitItem() is CatBreedListViewState.Loading)
            assertTrue(awaitItem() is CatBreedListViewState.Loaded)
            viewModel.searchCatBreeds("name")
            assertTrue(awaitItem() is CatBreedListViewState.Loading)
            assertTrue(awaitItem() is CatBreedListViewState.EmptySearchResults)
        }
    }

    @Test
    fun `Given there is an error getting local and remote search results When cat breeds are searched Then emit a empty serach view state`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val catBreedDTOs = listOf(
            CatBreedDTO("id1", "one, two", "description1", "origin1", "name1", "id", Image("image1")),
            CatBreedDTO("id2", "three, four", "description2", "origin2", "name2","id", Image("image2"))
        )


        val catBreedsDao = FakeCatBreedsDao()
        val catApi = FakeCatApi(catBreedList = catBreedDTOs)

        val breedListRepository = BreedRepositoryImplementation(
            BreedLocalDataSourceImplementation(catBreedsDao),
            BreedRemoteDataSourceImplementation(catApi),
            dispatcher
        )

        val favoriteCatBreedsGateway = FavoriteCatBreedsGatewayImplementation(catBreedsDao, dispatcher)

        val breedListInteractor = BreedListInteractorImplementation(
            breedListRepository, favoriteCatBreedsGateway, dispatcher
        )

        val favoriteCatBreedsInteractor = FavoriteCatBreedsInteractorImplementation(
            favoriteCatBreedsGateway, dispatcher
        )

        val viewModel = CatBreedListViewModel(
            breedListInteractor = breedListInteractor,
            favoriteCatBreedsInteractor = favoriteCatBreedsInteractor
        )

        viewModel.viewState.test {
            assertTrue(awaitItem() is CatBreedListViewState.Loading)
            assertTrue(awaitItem() is CatBreedListViewState.Loaded)
            viewModel.searchCatBreeds("name")
            assertTrue(awaitItem() is CatBreedListViewState.Loading)
            assertTrue(awaitItem() is CatBreedListViewState.Error)
        }
    }
}