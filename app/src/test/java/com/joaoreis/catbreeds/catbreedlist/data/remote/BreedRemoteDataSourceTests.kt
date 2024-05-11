package com.joaoreis.catbreeds.catbreedlist.data.remote

import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import com.joaoreis.catbreeds.Result
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BreedRemoteDataSourceTests {
    @Test
    fun `Given there are cat breeds available in the API When cat breeds are requested Then return list of cat breeds from API`() {
        val catBreeds = listOf(
            CatBreedDTO("id1", "one, two", "description1", "origin1", "name1", Image("image1")),
            CatBreedDTO("id2", "three, four", "description2", "origin2", "name2", Image("image2"))
        )


        val expectedResult: Result<List<CatBreed>> = Result.Success(
            listOf(
                CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("one","two")),
                CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("three","four"))
            )
        )

        val remoteDataSource = BreedRemoteDataSourceImplementation(
            catApi = FakeCatApi(catBreeds)
        )

        assertEquals(expectedResult, remoteDataSource.getBreedList())
    }

    @Test
    fun `Given there is an error fetching cat breeds from the API When cat breeds are requested Then return error`() {
        val remoteDataSource = BreedRemoteDataSourceImplementation(
            catApi = FakeCatApi()
        )

        assertTrue(remoteDataSource.getBreedList() is Result.Error)
    }
}