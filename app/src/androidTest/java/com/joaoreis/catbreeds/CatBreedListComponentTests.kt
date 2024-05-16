package com.joaoreis.catbreeds

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotEnabled
import androidx.compose.ui.test.isOff
import androidx.compose.ui.test.isOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.ImeAction
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedEntity
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedsDao
import com.joaoreis.catbreeds.catbreedlist.data.local.di.DatabaseModule
import com.joaoreis.catbreeds.catbreedlist.data.remote.di.BaseUrlModule
import com.joaoreis.catbreeds.util.FakeCatBreedsDao
import com.joaoreis.catbreeds.util.TestData
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

@UninstallModules(BaseUrlModule::class, DatabaseModule::class)
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class CatBreedListComponentTests {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @BindValue
    @JvmField
    var baseUrl: String = ""

    @BindValue
    @JvmField
    var catBreedsDao: CatBreedsDao = FakeCatBreedsDao()

    lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)

        baseUrl = mockWebServer.url("/").toString()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun GivenThereAreLocalCatBreedsWhenCatBreedsScreenIsShownThenDisplayListOfCatBreeds() {
        catBreedsDao = FakeCatBreedsDao(
            getCatBreedsResult = listOf(
                CatBreedEntity(
                    "id1",
                    "description1",
                    "origin1",
                    "Abyssinian",
                    "image1",
                    listOf("one", "two"),
                    false
                ),
                CatBreedEntity(
                    "id2",
                    "description2",
                    "origin2",
                    "Aegean",
                    "image2",
                    listOf("three", "four"),
                    false
                )
            )
        )
        hiltRule.inject()

        launch(MainActivity::class.java)

        composeTestRule.mainClock.autoAdvance = true
        composeTestRule.waitForIdle()

        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .assertCountEquals(2)

        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .get(0)
            .assert(hasText("Abyssinian", substring = true))

        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .get(1)
            .assert(hasText("Aegean", substring = true))

    }

    @Test
    fun GivenThereAreNoLocalCatBreedsAndThereAreRemoteBreedsWhenCatBreedsScreenIsShownThenDisplayListOfRemoteCatBreeds() {
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(TestData.CAT_BREED_LIST))

        baseUrl = mockWebServer.url("/").toString()
        hiltRule.inject()

        launch(MainActivity::class.java)

        composeTestRule.mainClock.autoAdvance = true
        composeTestRule.waitForIdle()

        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .assertCountEquals(2)

        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .get(0)
            .assert(hasText("Abyssinian", substring = true))

        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .get(1)
            .assert(hasText("Aegean", substring = true))
    }

    @Test
    fun GivenThereIsAnErrorFetchingCatBreedsWhenCatBreedsScreenIsShownThenDisplayAnErrorMessage() {
        mockWebServer.enqueue(MockResponse().setResponseCode(400))

        baseUrl = mockWebServer.url("/").toString()
        hiltRule.inject()

        launch(MainActivity::class.java)

        composeTestRule.mainClock.autoAdvance = true
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("CatBreedListError")
            .onChildren()
            .filter(hasText("No cat breeds found"))
            .get(0)
            .isDisplayed()
    }

    @Test
    fun GivenCatBreedsAreLoadingWhenCatBreedsScreenIsShownThenDisplayALoadingIndicator() {
        mockWebServer.enqueue(MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE))

        baseUrl = mockWebServer.url("/").toString()
        hiltRule.inject()

        launch(MainActivity::class.java)

        composeTestRule.mainClock.autoAdvance = true
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("CatBreedsLoading")
            .assertIsDisplayed()
    }

    @Test
    fun GivenCatBreedsAreLoadedWhenCatBreedsAreAddedToFavoritesThenrShouldUpdateFavoriteIcon() {
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(TestData.CAT_BREED_LIST))

        baseUrl = mockWebServer.url("/").toString()
        hiltRule.inject()

        launch(MainActivity::class.java)

        composeTestRule.mainClock.autoAdvance = true
        composeTestRule.waitForIdle()

        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .get(0)
            .onChildren()
            .filter(hasTestTag("FavoriteToggleButton"))
            .get(0)
            .assert(isOff())
            .performClick()


        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .get(1)
            .onChildren()
            .filter(hasTestTag("FavoriteToggleButton"))
            .get(0)
            .assert(isOff())
            .performClick()


        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .get(0)
            .onChildren()
            .filter(hasTestTag("FavoriteToggleButton"))
            .get(0)
            .assert(isOn())


        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .get(1)
            .onChildren()
            .filter(hasTestTag("FavoriteToggleButton"))
            .get(0)
            .assert(isOn())
    }

    @Test
    fun GivenCatBreedsAreLoadedWhenCatBreedsAreRemovedFromFavoritesThenrShouldUpdateFavoriteIcon() {
        baseUrl = mockWebServer.url("/").toString()
        catBreedsDao = FakeCatBreedsDao(
            getCatBreedsResult = listOf(
                CatBreedEntity(
                    "id1",
                    "description1",
                    "origin1",
                    "Abyssinian",
                    "image1",
                    listOf("one", "two"),
                    true
                ),
                CatBreedEntity(
                    "id2",
                    "description2",
                    "origin2",
                    "Aegean",
                    "image2",
                    listOf("three", "four"),
                    true
                )
            )
        )
        hiltRule.inject()

        launch(MainActivity::class.java)

        composeTestRule.mainClock.autoAdvance = true
        composeTestRule.waitForIdle()

        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .get(0)
            .onChildren()
            .filter(hasTestTag("FavoriteToggleButton"))
            .get(0)
            .assert(isOn())
            .performClick()


        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .get(0)
            .onChildren()
            .filter(hasTestTag("FavoriteToggleButton"))
            .get(0)
            .assert(isOff())
    }

    @Test
    fun GivenCatBreedsAreLoadedWhenASearchIsPerformedThenShouldDisplaySearchResults() {
        baseUrl = mockWebServer.url("/").toString()
        catBreedsDao = FakeCatBreedsDao(
            getCatBreedsResult = listOf(
                CatBreedEntity(
                    "id1",
                    "description1",
                    "origin1",
                    "Abyssinian",
                    "image1",
                    listOf("one", "two"),
                    true
                ),
                CatBreedEntity(
                    "id2",
                    "description2",
                    "origin2",
                    "Aegean",
                    "image2",
                    listOf("three", "four"),
                    true
                )
            ),
            searchResult = listOf(
                CatBreedEntity(
                    "id1",
                    "description1",
                    "origin1",
                    "Abyssinian",
                    "image1",
                    listOf("one", "two"),
                    true
                )
            )
        )
        hiltRule.inject()

        launch(MainActivity::class.java)

        composeTestRule.mainClock.autoAdvance = true
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("SearchField")
            .performTextInput("Aby")

        composeTestRule.onNode(hasImeAction(ImeAction.Search)).performImeAction()

        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .assertCountEquals(1)

        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .get(0)
            .assert(hasText("Abyssinian", substring = true))
    }

    @Test
    fun GivenCatBreedsAreLoadedWhenASearchIsPerformedAndThereAreNoLocalResultsButThereAreRemoteResultsThenShouldDisplaySearchResults() {
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(TestData.CAT_BREED_LIST))
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(TestData.CAT_BREED_SEARCH_LIST))

        baseUrl = mockWebServer.url("/").toString()
        catBreedsDao = FakeCatBreedsDao()
        hiltRule.inject()

        launch(MainActivity::class.java)

        composeTestRule.mainClock.autoAdvance = true
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("SearchField")
            .performTextInput("Aby")

        composeTestRule.onNode(hasImeAction(ImeAction.Search)).performImeAction()

        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .assertCountEquals(1)

        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .get(0)
            .assert(hasText("Abyssinian", substring = true))
    }

    @Test
    fun GivenCatBreedsAreLoadedWhenASearchIsPerformedAndThereIsAnErrorThenShouldDisplayAnErrorMessage() {
        baseUrl = mockWebServer.url("/").toString()
        catBreedsDao = FakeCatBreedsDao(
            getCatBreedsResult = listOf(
                CatBreedEntity(
                    "id1",
                    "description1",
                    "origin1",
                    "Abyssinian",
                    "image1",
                    listOf("one", "two"),
                    true
                ),
                CatBreedEntity(
                    "id2",
                    "description2",
                    "origin2",
                    "Aegean",
                    "image2",
                    listOf("three", "four"),
                    true
                )
            )
        )
        hiltRule.inject()

        launch(MainActivity::class.java)

        composeTestRule.mainClock.autoAdvance = true
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("SearchField")
            .performTextInput("Aby")

        composeTestRule.onNode(hasImeAction(ImeAction.Search)).performImeAction()

        composeTestRule
            .onNodeWithText("An error occurred")
            .isDisplayed()
    }
}