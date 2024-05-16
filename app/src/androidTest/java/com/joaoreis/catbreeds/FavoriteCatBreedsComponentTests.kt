package com.joaoreis.catbreeds

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isOn
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedEntity
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedsDao
import com.joaoreis.catbreeds.catbreedlist.data.local.di.DatabaseModule
import com.joaoreis.catbreeds.util.FakeCatBreedsDao
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UninstallModules(DatabaseModule::class)
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class FavoriteCatBreedsComponentTests {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @BindValue
    @JvmField
    var catBreedsDao: CatBreedsDao = FakeCatBreedsDao()

    @Test
    fun GivenThereAreFavoriteCatBreedsWhenFavoriteCatBreedsScreenIsShownThenDisplayListOfFavoriteCatBreeds() {
        catBreedsDao = FakeCatBreedsDao(
            favoritesResult = listOf(
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

        ActivityScenario.launch(MainActivity::class.java)

        composeTestRule.mainClock.autoAdvance = true
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Favorites")
            .performClick()

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
    fun GivenThereAreNoFavoriteCatBreedsWhenFavoriteCatBreedsScreenIsShownThenDisplayAMessage() {
        catBreedsDao = FakeCatBreedsDao(
            favoritesResult = listOf()
        )
        hiltRule.inject()

        ActivityScenario.launch(MainActivity::class.java)

        composeTestRule.mainClock.autoAdvance = true
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Favorites")
            .performClick()

        composeTestRule
            .onNodeWithTag("EmptyFavorites")
            .onChildren()
            .filter(hasText("You have no favorites yet! Go and add some"))
            .assertCountEquals(1)
    }

    @Test
    fun GivenThereAreFavoriteCatBreedsDisplayerWhenACatBreedIsRemovedFromFavoritesThenItShouldDisappearFromScreen() {
        catBreedsDao = FakeCatBreedsDao(
            favoritesResult = listOf(
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

        ActivityScenario.launch(MainActivity::class.java)

        composeTestRule.mainClock.autoAdvance = true
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Favorites")
            .performClick()

        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .get(0)
            .onChildren()
            .filter(hasTestTag("FavoriteToggleButton"))
            .get(0)
            .assert(isOn())
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .assertCountEquals(1)

        composeTestRule.onAllNodesWithTag("CatBreedItem")
            .get(0)
            .assert(hasText("Aegean", substring = true))
    }
}