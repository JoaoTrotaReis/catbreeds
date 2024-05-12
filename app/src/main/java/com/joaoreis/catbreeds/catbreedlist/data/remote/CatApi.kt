package com.joaoreis.catbreeds.catbreedlist.data.remote

import retrofit2.http.GET
import retrofit2.http.Headers

interface CatApi {
    @GET("breeds")
    @Headers("x-api-key: live_jyqZRbe5KphqF9jN3acL2spgJ6KwjppqrAK6wZG3WdeORIqS86YcMtgc2qES81VE")
    suspend fun getCatBreeds(): List<CatBreedDTO>
}