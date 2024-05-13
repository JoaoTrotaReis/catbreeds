package com.joaoreis.catbreeds.catbreedlist.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CatBreedsDao {
    @Query("SELECT * FROM catbreedentity")
    fun getCatBreeds(): List<CatBreedEntity>

    @Insert
    fun saveCatBreeds(catBreeds: List<CatBreedEntity>)

    @Query("SELECT * FROM catbreedentity where isFavorite=1")
    fun getFavoriteBreeds(): List<CatBreedEntity>
}