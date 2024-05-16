package com.joaoreis.catbreeds

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isOff
import androidx.compose.ui.test.isOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario.launch
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
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UninstallModules(BaseUrlModule::class, DatabaseModule::class)
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class CatBreedDetailsComponentTests {
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
    fun GivenACatBreedInLocalStorageWhenDetailsScreenIsShownThenShowDisplayCatBreedDetails() {
        baseUrl = mockWebServer.url("/").toString()
        catBreedsDao = FakeCatBreedsDao(
            getCatBreedsResult = listOf(
                CatBreedEntity(
                    "id1",
                    "Description",
                    "origin",
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
            ), findResult = CatBreedEntity(
                "id1",
                "Description",
                "origin",
                "Abyssinian",
                "image1",
                listOf("one", "two"),
                false
            )
        )
        hiltRule.inject()

        launch(MainActivity::class.java)

        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .get(0)
            .performClick()

        composeTestRule.onNodeWithText("Abyssinian")
            .isDisplayed()

        composeTestRule.onNodeWithText("Description")
            .isDisplayed()

        composeTestRule.onNodeWithText("Origin: origin")
            .isDisplayed()

        composeTestRule.onNodeWithTag("FavoriteToggleButton")
            .assert(isOff())
            .isDisplayed()
    }

    @Test
    fun GivenACatBreedInApiWhenDetailsScreenIsShownThenShowDisplayCatBreedDetails() {
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(TestData.CAT_BREED_DETAILS))

        baseUrl = mockWebServer.url("/").toString()
        catBreedsDao = FakeCatBreedsDao(
            getCatBreedsResult = listOf(
                CatBreedEntity(
                    "id1",
                    "Description",
                    "origin",
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

        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .get(0)
            .performClick()

        composeTestRule.onNodeWithText("Abyssinian")
            .isDisplayed()

        composeTestRule.onNodeWithText("The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.")
            .isDisplayed()

        composeTestRule.onNodeWithText("Origin: Egypt")
            .isDisplayed()

        composeTestRule.onNodeWithTag("FavoriteToggleButton")
            .isDisplayed()
    }

    @Test
    fun GivenACatBreedInLocalStorageWhenDetailsScreenIsShownAndItIsAddedToFavoritesThenShowDisplayUpdateFavoriteIcon() {
        baseUrl = mockWebServer.url("/").toString()
        catBreedsDao = FakeCatBreedsDao(
            getCatBreedsResult = listOf(
                CatBreedEntity(
                    "id1",
                    "Description",
                    "origin",
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
            ), findResult = CatBreedEntity(
                "id1",
                "Description",
                "origin",
                "Abyssinian",
                "image1",
                listOf("one", "two"),
                false
            )
        )
        hiltRule.inject()

        launch(MainActivity::class.java)

        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .get(0)
            .performClick()

        composeTestRule.onNodeWithTag("FavoriteToggleButton")
            .performClick()
            .assert(isOn())
    }
}