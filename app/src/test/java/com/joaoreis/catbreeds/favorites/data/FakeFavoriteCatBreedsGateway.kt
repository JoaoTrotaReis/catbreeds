package com.joaoreis.catbreeds.favorites.data

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import com.joaoreis.catbreeds.catbreedlist.domain.FavoriteUpdate
import com.joaoreis.catbreeds.favorites.domain.FavoriteCatBreedsGateway
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class FakeFavoriteCatBreedsGateway(
    val favoriteBreedsResult: Result<List<CatBreed>> = Result.Success(listOf()),
    val favoriteUdpdatesFlow: SharedFlow<FavoriteUpdate> = MutableSharedFlow()
): FavoriteCatBreedsGateway {
    val favorites = HashMap<String, Boolean>()
    override val favoriteUpdates = favoriteUdpdatesFlow

    override suspend fun getFavoriteCatBreeds(): Result<List<CatBreed>> = favoriteBreedsResult
    override suspend fun toggleFavorite(id: String, isFavorite: Boolean) { favorites[id] = isFavorite }

    suspend fun simulateFavoriteUpdate(id: String, isFavorite: Boolean) {
        //favoriteUpdates.emit(FavoriteUpdate(id, isFavorite))
    }
}