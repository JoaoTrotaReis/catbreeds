package com.joaoreis.catbreeds.catbreedlist.domain

data class CatBreed(
    val breedId: String,
    val breedName: String,
    val breedImage: String,
    val origin: String,
    val description: String,
    val temperament: List<String>
)