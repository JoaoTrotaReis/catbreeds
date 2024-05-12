package com.joaoreis.catbreeds.catbreedlist.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed

@Entity
data class CatBreedEntity(
    @PrimaryKey val id: String,
    val description: String,
    val origin: String,
    val name: String,
    val image: String,
    val temperament: List<String>
)

fun CatBreedEntity.toDomainModel(): CatBreed = CatBreed(
    breedId = this.id,
    breedName = this.name,
    breedImage = this.image,
    origin = this.origin,
    description = this.description,
    temperament = this.temperament
)

fun CatBreed.toDatabaseEntity(): CatBreedEntity = CatBreedEntity(
    id = this.breedId,
    name = this.breedName,
    image = this.breedImage ?: "",
    origin = this.origin,
    description = this.description,
    temperament = this.temperament
)