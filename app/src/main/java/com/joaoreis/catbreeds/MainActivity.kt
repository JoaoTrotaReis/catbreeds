package com.joaoreis.catbreeds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.joaoreis.catbreeds.catbreedlist.presentation.CatBreedListViewModel
import com.joaoreis.catbreeds.catbreedlist.presentation.CatBreedListViewState
import com.joaoreis.catbreeds.catbreedlist.ui.CatBreedsListScreen
import com.joaoreis.catbreeds.ui.theme.CatBreedsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: CatBreedListViewModel = hiltViewModel()
            CatBreedsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewState = viewModel.viewState.collectAsState().value
                    when(viewState) {
                        is CatBreedListViewState.Loaded -> {
                            CatBreedsListScreen(
                                viewState.catBreedItems,
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        else -> {}
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CatBreedsTheme {
        Greeting("Android")
    }
}