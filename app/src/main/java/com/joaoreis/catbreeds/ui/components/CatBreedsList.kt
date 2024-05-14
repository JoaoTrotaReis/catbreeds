package com.joaoreis.catbreeds.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joaoreis.catbreeds.catbreedlist.presentation.CatBreedViewItem
import com.joaoreis.catbreeds.catbreedlist.ui.CatBreedItem

@Composable
fun CatBreedsList(
    catBreedItems: List<CatBreedListItem>,
    onFavoriteToggle: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(128.dp),
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        ),
        horizontalArrangement = Arrangement.Center,
        content = {
            items(catBreedItems) {
                CatBreedItem(
                    id = it.id,
                    imageUrl = it.image,
                    breedName = it.breedName,
                    isFavorite = it.isFavorite,
                    onFavoriteToggle = onFavoriteToggle
                )
            }
        }
    )

}

@Preview
@Composable
fun CatBreedListScreen_Preview() {
    CatBreedsList(catBreedItems = listOf(
        CatBreedListItem(
            id = "id",
            image = "https://cdn2.thecatapi.com/images/ozEvzdVM-.jpg",
            breedName = "Abyssinian",
            isFavorite = true
        ),
        CatBreedListItem(
            id = "id",
            image = "https://cdn2.thecatapi.com/images/ozEvzdVM-.jpg",
            breedName = "Abyssinian",
            isFavorite = true
        ),
        CatBreedListItem(
            id = "id",
            image = "https://cdn2.thecatapi.com/images/ozEvzdVM-.jpg",
            breedName = "Abyssinian",
            isFavorite = true
        ),
        CatBreedListItem(
            id = "id",
            image = "https://cdn2.thecatapi.com/images/ozEvzdVM-.jpg",
            breedName = "Abyssinian",
            isFavorite = true
        ),
        CatBreedListItem(
            id = "id",
            image = "https://cdn2.thecatapi.com/images/ozEvzdVM-.jpg",
            breedName = "Abyssinian",
            isFavorite = true
        ),
        CatBreedListItem(
            id = "id",
            image = "https://cdn2.thecatapi.com/images/ozEvzdVM-.jpg",
            breedName = "Abyssinian",
            isFavorite = true
        ),
        CatBreedListItem(
            id = "id",
            image = "https://cdn2.thecatapi.com/images/ozEvzdVM-.jpg",
            breedName = "Abyssinian",
            isFavorite = true
        ),
        CatBreedListItem(
            id = "id",
            image = "https://cdn2.thecatapi.com/images/ozEvzdVM-.jpg",
            breedName = "Abyssinian",
            isFavorite = true
        ),
        CatBreedListItem(
            id = "id",
            image = "https://cdn2.thecatapi.com/images/ozEvzdVM-.jpg",
            breedName = "Abyssinian",
            isFavorite = true
        ),
        CatBreedListItem(
            id = "id",
            image = "https://cdn2.thecatapi.com/images/ozEvzdVM-.jpg",
            breedName = "Abyssinian",
            isFavorite = true
        )
    ), onFavoriteToggle = { _, _ -> })
}