package com.joaoreis.catbreeds.details.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CatBreedDetailsRepositoryTests {

    @Test
    fun `Given a cat breed id And there are details stored locally for that id When details are request Then return success with the local breed details`() = runTest {
        val catBreed = CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("temperament"), false)
        val expectedResult = Result.Success(catBreed)

        val repository = CatBreedDetailsRepositoryImplementation(
            localDataSource = FakeBreedDetailsLocalSource(Result.Success(catBreed)),
            remoteDataSource = FakeBreedDetailsRemoteSource(Result.Error()),
            dispatcher = StandardTestDispatcher(testScheduler)
        )

        val actualResult = repository.getCatBreed("id")

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given a cat breed id And there are no details stored locally for that id And there are remote details When details are request Then return success with the remote breed details`() = runTest {
        val catBreed = CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("temperament"), false)
        val expectedResult = Result.Success(catBreed)

        val repository = CatBreedDetailsRepositoryImplementation(
            localDataSource = FakeBreedDetailsLocalSource(Result.Error()),
            remoteDataSource = FakeBreedDetailsRemoteSource(Result.Success(catBreed)),
            dispatcher = StandardTestDispatcher(testScheduler)
        )

        val actualResult = repository.getCatBreed("id")

        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Given a cat breed id And there are no local or remote details for that id When details are request Then return error`() = runTest {
        val repository = CatBreedDetailsRepositoryImplementation(
            localDataSource = FakeBreedDetailsLocalSource(Result.Error()),
            remoteDataSource = FakeBreedDetailsRemoteSource(Result.Error()),
            dispatcher = StandardTestDispatcher(testScheduler)
        )

        val actualResult = repository.getCatBreed("id")

        assertTrue(actualResult is Result.Error)
    }
}