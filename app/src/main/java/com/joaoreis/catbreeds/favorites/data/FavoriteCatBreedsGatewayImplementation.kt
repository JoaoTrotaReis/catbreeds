package com.joaoreis.catbreeds.favorites.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.data.local.CatBreedsDao
import com.joaoreis.catbreeds.catbreedlist.data.local.toDomainModel
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import com.joaoreis.catbreeds.catbreedlist.domain.FavoriteUpdate
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsGateway
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext

class FavoriteCatBreedsGatewayImplementation(
    private val catBreedsDao: CatBreedsDao,
    val dispatcher: CoroutineDispatcher
): FavoriteCatBreedsGateway {
    val _favoriteUpdates = MutableSharedFlow<FavoriteUpdate>()
    override val favoriteUpdates: Flow<FavoriteUpdate> = _favoriteUpdates

    override suspend fun getFavoriteCatBreeds(): Result<List<CatBreed>> = withContext(dispatcher) {
        try {
            Result.Success(catBreedsDao.getFavoriteBreeds().map { it.toDomainModel() })
        } catch (e: Exception) {
            Result.Error()
        }
    }

    override suspend fun toggleFavorite(id: String, isFavorite: Boolean) {
        withContext(dispatcher) {
            catBreedsDao.updateCatBreed(id, isFavorite)
             _favoriteUpdates.emit(FavoriteUpdate(id, isFavorite))
        }
    }
}