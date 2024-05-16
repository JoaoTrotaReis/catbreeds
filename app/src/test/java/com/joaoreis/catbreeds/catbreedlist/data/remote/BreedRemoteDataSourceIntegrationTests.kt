package com.joaoreis.catbreeds.catbreedlist.data.remote

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class BreedRemoteDataSourceIntegrationTests {
    @Test
    fun `Given there are cat breeds available in the API When cat breeds are requested Then should call correct API endpoint And return list of cat breeds from API`() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(MockResponse().setBody(TestData.CAT_BREED_LIST))

            val catApi = createCatApi(server.url("/").toString())

            val expectedResult: Result<List<CatBreed>> = Result.Success(
                listOf(
                    CatBreed(
                        "abys",
                        "Abyssinian",
                        "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg",
                        "Egypt",
                        "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
                        listOf("Active", "Energetic", "Independent", "Intelligent", "Gentle"),
                        false
                    ),
                    CatBreed(
                        "aege",
                        "Aegean",
                        "https://cdn2.thecatapi.com/images/ozEvzdVM-.jpg",
                        "Greece",
                        "Native to the Greek islands known as the Cyclades in the Aegean Sea, these are natural cats, meaning they developed without humans getting involved in their breeding. As a breed, Aegean Cats are rare, although they are numerous on their home islands. They are generally friendly toward people and can be excellent cats for families with children.",
                        listOf("Affectionate", "Social", "Intelligent", "Playful", "Active"),
                        false
                    )
                )
            )

            val remoteDataSource = BreedRemoteDataSourceImplementation(
                catApi = catApi
            )

            assertEquals(expectedResult, remoteDataSource.getBreedList())

            val request = server.takeRequest()

            assertEquals("/breeds", request.path.toString())
            assertEquals("GET", request.method.toString())

            server.shutdown()
        }

    @Test
    fun `Given there is an error fetching cat breeds from the API When cat breeds are requested Then should call correct API endpoint and return error`() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(MockResponse().setResponseCode(404))

            val catApi = createCatApi(server.url("/").toString())

            val remoteDataSource = BreedRemoteDataSourceImplementation(
                catApi = catApi
            )

            assertTrue(remoteDataSource.getBreedList() is Result.Error)

            val request = server.takeRequest()

            assertEquals("/breeds", request.path.toString())
            assertEquals("GET", request.method.toString())

            server.shutdown()
        }

    @Test
    fun `Given a search term And there are cat breeds available in the API When cat breeds are requested Then should call correct API endpoint and return list of cat breeds from API`() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(MockResponse().setBody(TestData.CAT_BREED_SEARCH_LIST))

            val catApi = createCatApi(server.url("/").toString())

            val expectedResult: Result<List<CatBreed>> = Result.Success(
                listOf(
                    CatBreed(
                        "abys",
                        "Abyssinian",
                        "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg",
                        "Egypt",
                        "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
                        listOf("Active", "Energetic", "Independent", "Intelligent", "Gentle"),
                        false
                    )
                )
            )

            val remoteDataSource = BreedRemoteDataSourceImplementation(
                catApi = catApi
            )

            assertEquals(expectedResult, remoteDataSource.searchBreed("name"))

            val request = server.takeRequest()

            assertEquals("/breeds/search?attach_image=1&q=name", request.path.toString())
            assertEquals("GET", request.method.toString())

            server.shutdown()
        }

    @Test
    fun `Given there is an error searching cat breeds from the API When cat breeds are requested Then should call correct API endpoint and return error`() =
        runTest {
            val server = MockWebServer()
            server.start()
            server.enqueue(MockResponse().setResponseCode(404))

            val catApi = createCatApi(server.url("/").toString())

            val remoteDataSource = BreedRemoteDataSourceImplementation(
                catApi = catApi
            )

            assertTrue(remoteDataSource.searchBreed("name") is Result.Error)

            val request = server.takeRequest()

            assertEquals("/breeds/search?attach_image=1&q=name", request.path.toString())
            assertEquals("GET", request.method.toString())

            server.shutdown()
        }

    private fun createCatApi(baseUrl: String): CatApi {
        return Retrofit.Builder()
            .addConverterFactory(Json {
                ignoreUnknownKeys = true
            }.asConverterFactory("application/json".toMediaType()))
            .baseUrl(baseUrl)
            .client(OkHttpClient.Builder().build())
            .build()
            .create(CatApi::class.java)
    }
}