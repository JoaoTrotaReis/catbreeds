package com.joaoreis.catbreeds.details.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.joaoreis.catbreeds.R
import com.joaoreis.catbreeds.details.presentation.CatBreedDetails
import com.joaoreis.catbreeds.details.presentation.CatBreedDetailsViewModel
import com.joaoreis.catbreeds.details.presentation.CatBreedDetailsViewState
import com.joaoreis.catbreeds.ui.components.FavoriteButton

@Composable
fun CatBreedDetailsScreen(
    modifier: Modifier,
    viewModel: CatBreedDetailsViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadDetails()
    }

    when (val state = viewModel.viewState.collectAsState().value) {
        CatBreedDetailsViewState.Error -> {}
        CatBreedDetailsViewState.Loading -> {}
        is CatBreedDetailsViewState.Loaded -> {
            BreedDetailsComponent(
                modifier = modifier,
                catBreedDetails = state.catBreedDetails,
                onFavoriteToggle = { id, favorite ->
                    viewModel.onFavoriteToggle(
                        id, favorite
                    )
                },
                onBackPressed)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BreedDetailsComponent(
    modifier: Modifier = Modifier,
    catBreedDetails: CatBreedDetails,
    onFavoriteToggle: (String, Boolean) -> Unit,
    onBackPressed: () -> Unit
) {
    Box {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
        ) {
            AsyncImage(
                model = catBreedDetails.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.placeholder),
                fallback = painterResource(id = R.drawable.placeholder),
                placeholder = painterResource(id = R.drawable.placeholder),
                modifier = Modifier
                    .height(256.dp)

            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .clip(shape = RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                    .padding(
                        12.dp
                    )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = catBreedDetails.breedName, fontSize = 40.sp)
                }

                Spacer(modifier = Modifier.padding(12.dp))

                Text("Origin: " + catBreedDetails.origin)

                Spacer(modifier = Modifier.padding(12.dp))

                Text(catBreedDetails.descripton)

                Spacer(modifier = Modifier.padding(8.dp))

                FlowRow {
                    catBreedDetails.temperament.forEach {
                        SuggestionChip(
                            modifier = Modifier.padding(end = 4.dp),
                            onClick = {},
                            label = { Text(it) })
                    }
                }
            }
        }
        Surface(
            shape = CircleShape,
            modifier = Modifier.align(Alignment.TopStart)
                .padding(6.dp)
                .size(32.dp),
            color = Color(0x77FFFFFF)
        ) {
            Icon(
                modifier = Modifier.size(32.dp).clickable {
                    onBackPressed()
                },
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null,
            )
        }

        Surface(
            shape = CircleShape,
            modifier = Modifier.align(Alignment.TopEnd)
                .padding(6.dp)
                .size(32.dp),
            color = Color(0x77FFFFFF)
        ) {
            FavoriteButton(
                modifier = Modifier,
                onToggle = { onFavoriteToggle(catBreedDetails.id, it) },
                isFavorite = catBreedDetails.isFavorite
            )
        }
    }
}

@Preview
@Composable
fun BreedDetailsComponent_Preview() {
    BreedDetailsComponent(
        catBreedDetails = CatBreedDetails(
            "id", "image", "Abyssinian", "Description", "origin", listOf("temperament"), true
        ), onFavoriteToggle = { _, _ -> }, onBackPressed = {}
    )
}