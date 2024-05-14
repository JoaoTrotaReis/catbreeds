package com.joaoreis.catbreeds.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
    onToggle: (Boolean) -> Unit,
    isFavorite: Boolean
) {
    IconToggleButton(
        checked = isFavorite,
        onCheckedChange = {
            onToggle(it)
        }
    ) {
        Icon(
            tint = color,

            imageVector = if (isFavorite) {
                Icons.Filled.Favorite
            } else {
                Icons.Default.FavoriteBorder
            },
            contentDescription = null
        )
    }

}