package com.joaoreis.catbreeds.catbreedlist.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.joaoreis.catbreeds.R
import com.joaoreis.catbreeds.ui.components.FavoriteButton

@Composable
fun CatBreedItem(imageUrl: String, breedName: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.padding(12.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Column {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.placeholder),
                fallback = painterResource(id = R.drawable.placeholder),
                placeholder = painterResource(id = R.drawable.placeholder),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .height(128.dp)
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(breedName)
        }
        FavoriteButton(onToggle = {}, isFavorite = false)
    }
}

@Preview
@Composable
fun CatBreedItem_Preview() {
    CatBreedItem(
        imageUrl = "https://cdn2.thecatapi.com/images/ozEvzdVM-.jpg",
        breedName = "Abyssinian"
    )
}