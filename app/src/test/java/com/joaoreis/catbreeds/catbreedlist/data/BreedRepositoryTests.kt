package com.joaoreis.catbreeds.catbreedlist.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BreedRepositoryTests {

    @Test
    fun `Given there are breeds in local data source When list of breeds is requested Then return the local data source breeds`() = runTest {
        val catBreeds = listOf(
            CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("temperament")),
            CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("temperament"))
        )

        val localDataSource = FakeLocalDataSource(Result.Success(catBreeds))

        val repository = BreedRepositoryImplementation(
            localDataSource = localDataSource,
            remoteDataSource = FakeRemoteDataSource(Result.Error()),
            dispatcher = StandardTestDispatcher(testScheduler)
        )

        val actualResult = repository.getCatBreeds()

        assertEquals(Result.Success(catBreeds), actualResult)
    }

    @Test
    fun `Given there are no breeds in local data source And there are breeds in remote data source When list of breeds is requested Then return the remote data source breeds`() = runTest {
        val catBreeds = listOf(
            CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("temperament")),
            CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("temperament"))
        )

        val localDataSource = FakeLocalDataSource(Result.Error())
        val remoteDataSource = FakeRemoteDataSource(Result.Success(catBreeds))

        val repository = BreedRepositoryImplementation(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            dispatcher = StandardTestDispatcher(testScheduler)
        )

        val actualResult = repository.getCatBreeds()

        assertEquals(Result.Success(catBreeds), actualResult)
    }

    @Test
    fun `Given there are no breeds in local data source And there are no breeds in remote data source When list of breeds is requested Then return the an error`() = runTest {
        val localDataSource = FakeLocalDataSource(Result.Error())
        val remoteDataSource = FakeRemoteDataSource(Result.Error())

        val repository = BreedRepositoryImplementation(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            dispatcher = StandardTestDispatcher(testScheduler)
        )

        val actualResult = repository.getCatBreeds()

        assertTrue(actualResult is Result.Error)
    }

    @Test
    fun `Given there are no breeds in local data source And there are breeds in remote data source When list of breeds is requested Then the remote breeds should be stored`() = runTest {
        val catBreeds = listOf(
            CatBreed("id1", "name1", "image1", "origin1", "description1", listOf("temperament")),
            CatBreed("id2", "name2", "image2", "origin2", "description2", listOf("temperament"))
        )

        val localDataSource = FakeLocalDataSource(Result.Error())
        val remoteDataSource = FakeRemoteDataSource(Result.Success(catBreeds))

        val repository = BreedRepositoryImplementation(
            localDataSource = localDataSource,
            remoteDataSource = remoteDataSource,
            dispatcher = StandardTestDispatcher(testScheduler)
        )

        repository.getCatBreeds()

        assertEquals(catBreeds, localDataSource.storedBreeds)
    }
}