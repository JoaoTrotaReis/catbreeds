package com.joaoreis.catbreeds.catbreedlist.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CatBreedsDao {
    @Query("SELECT * FROM catbreedentity")
    fun getCatBreeds(): List<CatBreedEntity>

    @Insert
    fun saveCatBreeds(catBreeds: List<CatBreedEntity>)

    @Query("SELECT * FROM catbreedentity where isFavorite=1")
    fun getFavoriteBreeds(): List<CatBreedEntity>

    @Query("UPDATE catbreedentity SET isFavorite = :isFavorite WHERE id = :id")
    fun updateCatBreed(id: String, isFavorite: Boolean)

    @Query("SELECT * FROM catbreedentity WHERE name LIKE :name")
    fun findCatBreedByName(name: String): List<CatBreedEntity>
}