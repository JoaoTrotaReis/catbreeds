package com.joaoreis.catbreeds.catbreedlist.data.remote

import retrofit2.http.GET
import retrofit2.http.Headers

interface CatApi {
    @GET("breeds")
    suspend fun getCatBreeds(): List<CatBreedDTO>
}