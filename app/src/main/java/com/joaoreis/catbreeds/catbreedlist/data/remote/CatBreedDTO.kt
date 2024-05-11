package com.joaoreis.catbreeds.catbreedlist.data.remote

import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import kotlinx.serialization.Serializable

@Serializable
data class CatBreedDTO(
    val id: String,
    val temperament: String,
    val description: String,
    val origin: String,
    val name: String,
    val image: Image
)

@Serializable
data class Image(
    val url: String
)

fun CatBreedDTO.toDomainModel(): CatBreed = CatBreed(
    breedId = this.id,
    breedName = this.name,
    breedImage = this.image.url,
    origin = this.origin,
    description = this.description,
    temperament = this.temperament.split(",").map { it.trim() }
)