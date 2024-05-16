package com.joaoreis.catbreeds.integration

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.data.local.BreedLocalDataSourceImplementation
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedDatabase
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedsDao
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class BreedLocalDataSourceIntegrationTests {
    private lateinit var catBreedsDao: CatBreedsDao
    private lateinit var db: CatBreedDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,  CatBreedDatabase::class.java).build()
        catBreedsDao = db.catBreedDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.clearAllTables()
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun GivenThereAreCatBreedsStoredInDBWhenCatBreedsAreRequestedThenReturnResult() {
        val breedLocalDataSource = BreedLocalDataSourceImplementation(catBreedsDao)
        val catBreeds: List<CatBreed> = listOf(
            CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("one","two"), false),
            CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("three","four"), false)
        )

        breedLocalDataSource.saveBreedList(catBreeds)

        val actualResult = breedLocalDataSource.getBreedList()

        assertEquals(Result.Success(catBreeds), actualResult)
    }

    @Test
    @Throws(Exception::class)
    fun GivenThereAreCatBreedsStoredInDBWhenCatBreedsAreSearchedThenReturnSearchResult() {
        val breedLocalDataSource = BreedLocalDataSourceImplementation(catBreedsDao)
        val catBreeds: List<CatBreed> = listOf(
            CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("one","two"), false),
            CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("three","four"), false)
        )

        val expectedResult = listOf(CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("three","four"), false))


        breedLocalDataSource.saveBreedList(catBreeds)

        val actualResult = breedLocalDataSource.searchBreed("name2")

        assertTrue(actualResult is Result.Success && actualResult.data == expectedResult)
    }
}