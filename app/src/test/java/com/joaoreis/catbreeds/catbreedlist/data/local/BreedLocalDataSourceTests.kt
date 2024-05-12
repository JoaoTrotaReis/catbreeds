package com.joaoreis.catbreeds.catbreedlist.data.local

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import org.junit.Assert.assertEquals
import org.junit.Test

class BreedLocalDataSourceTests {

    @Test
    fun `Given there are cat breeds in the database When cat breeds are requested Then return cat breeds`() {
        val catBreeds = listOf(
            CatBreedEntity("id1", "description1", "origin1", "name1", "image1", listOf("one","two")),
            CatBreedEntity("id2", "description2", "origin2", "name2", "image2", listOf("three","four"))
        )

        val expectedResult: Result<List<CatBreed>> = Result.Success(
            listOf(
                CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("one","two")),
                CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("three","four"))
            )
        )

        val localDataSource = BreedLocalDataSourceImplementation(
            FakeCatBreedsDao(catBreeds)
        )

        val actualResult = localDataSource.getBreedList()

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given a list of cat breeds When cat breeds list is saved Then it should be stored in database`() {
        val expectedResult = listOf(
            CatBreedEntity("id1", "description1", "origin1", "name1", "image1", listOf("one","two")),
            CatBreedEntity("id2", "description2", "origin2", "name2", "image2", listOf("three","four"))
        )

        val catBreeds: List<CatBreed> = listOf(
                CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("one","two")),
                CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("three","four"))
        )

        val catBreedsDao = FakeCatBreedsDao()

        val localDataSource = BreedLocalDataSourceImplementation(
            catBreedsDao
        )

        localDataSource.saveBreedList(catBreeds)

        assertEquals(expectedResult, catBreedsDao.savedCatBreeds)
    }
}