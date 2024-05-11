package com.joaoreis.catbreeds.catbreedlist.data.remote

import retrofit2.http.GET

interface CatApi {
    @GET("breeds")
    fun getCatBreeds(): List<CatBreedDTO>
}