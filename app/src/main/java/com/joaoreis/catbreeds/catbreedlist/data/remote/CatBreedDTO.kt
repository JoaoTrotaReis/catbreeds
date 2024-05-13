package com.joaoreis.catbreeds.catbreedlist.data.remote

import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatBreedDTO(
    val id: String,
    val temperament: String,
    val description: String,
    val origin: String,
    val name: String,
    @SerialName("reference_image_id") val referenceImageId: String? = null,
    val image: Image? = null
)

@Serializable
data class Image(
    val url: String
)

fun CatBreedDTO.toDomainModel(): CatBreed {
    val urlFromId = referenceImageId?.let {
        "https://cdn2.thecatapi.com/images/${referenceImageId}.jpg"
    } ?: ""

    return CatBreed(
        breedId = id,
        breedName = name,
        breedImage = image?.url ?: urlFromId,
        origin = origin,
        description = description,
        temperament = temperament.split(",").map { it.trim() },
        isFavorite = false
    )
}