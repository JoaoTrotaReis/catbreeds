package com.joaoreis.catbreeds.ui.components

import androidx.annotation.StringRes
import com.joaoreis.catbreeds.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    data object AllCatBreeds : Screen("catbreeds", R.string.all_cat_breeds)
    data object FavoriteCatBreeds : Screen("favorites", R.string.favorites)
    data object CatBreedDetails : Screen("details/{id}", R.string.all_cat_breeds) {
        fun buildNavRoute(id: String) = "details/${id}"
    }
}