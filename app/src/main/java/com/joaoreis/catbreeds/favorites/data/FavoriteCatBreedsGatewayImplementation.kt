package com.joaoreis.catbreeds.favorites.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedsDao
import com.joaoreis.catbreeds.catbreedlist.data.local.toDomainModel
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsGateway

class FavoriteCatBreedsGatewayImplementation(
    private val catBreedsDao: CatBreedsDao
): FavoriteCatBreedsGateway {
    override fun getFavoriteCatBreeds(): Result<List<CatBreed>> {
        return try {
            Result.Success(catBreedsDao.getCatBreeds().map { it.toDomainModel() })
        } catch (e: Exception) {
            Result.Error()
        }
    }
}