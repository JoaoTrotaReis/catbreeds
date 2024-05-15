package com.joaoreis.catbreeds.catbreedlist.data.remote

import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CatApi {
    @GET("breeds")
    suspend fun getCatBreeds(): List<CatBreedDTO>

    @GET("breeds/search?attach_image=1")
    suspend fun searchCatBreeds(@Query("q") searchTerm: String): List<CatBreedDTO>
}