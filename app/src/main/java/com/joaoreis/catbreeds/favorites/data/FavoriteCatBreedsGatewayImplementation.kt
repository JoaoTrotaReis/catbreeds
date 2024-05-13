package com.joaoreis.catbreeds.favorites.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedsDao
import com.joaoreis.catbreeds.catbreedlist.data.local.toDomainModel
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsGateway
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class FavoriteCatBreedsGatewayImplementation(
    private val catBreedsDao: CatBreedsDao,
    val dispatcher: CoroutineDispatcher
): FavoriteCatBreedsGateway {
    override suspend fun getFavoriteCatBreeds(): Result<List<CatBreed>> = withContext(dispatcher) {
        try {
            Result.Success(catBreedsDao.getCatBreeds().map { it.toDomainModel() })
        } catch (e: Exception) {
            Result.Error()
        }
    }
}