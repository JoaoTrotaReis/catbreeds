package com.joaoreis.catbreeds.favorites.domain

import com.joaoreis.catbreeds.Result
import com.joaoreis.catbreeds.catbreedlist.domain.CatBreed
import com.joaoreis.catbreeds.catbreedlist.domain.FavoriteUpdate
import kotlinx.coroutines.flow.Flow

interface FavoriteCatBreedsGateway {
    val favoriteUpdates: Flow<FavoriteUpdate>
    suspend fun getFavoriteCatBreeds(): Result<List<CatBreed>>
    suspend fun toggleFavorite(id: String, isFavorite: Boolean)
}