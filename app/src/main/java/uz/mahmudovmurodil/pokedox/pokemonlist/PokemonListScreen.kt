package uz.mahmudovmurodil.pokedox.pokemonlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import uz.mahmudovmurodil.pokedox.R
import uz.mahmudovmurodil.pokedox.data.models.PokeDexListEntry
import uz.mahmudovmurodil.pokedox.ui.theme.RobotoCondensed

@Composable
fun PokemonListScreen(
    navController: NavController, viewModel: PokemonListViewModel = hiltViewModel()
) {
    Surface(
        color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription = "Pokemon",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            )

            SearchBar(
                hint = "Search...", modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                viewModel.searchPokemon(it)
            }

            Spacer(modifier = Modifier.height(16.dp))

            PokemonList(navController = navController)
        }

    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier, hint: String = "", onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }
    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
                isHintDisplayed = text == ""
            },
            singleLine = true,
            maxLines = 1,
            textStyle = TextStyle(color = Color.Blue),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        )
        if (isHintDisplayed) {
            Text(
                text = hint, color = Color.LightGray, modifier = Modifier.padding(20.dp, 12.dp)
            )
        }
    }
}

@Composable
fun PokemonList(
    navController: NavController, viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokemonList by remember { viewModel.pokemonList }
    val loadError by remember { viewModel.loadError }
    val endReached by remember { viewModel.endReached }
    val isLoading by remember { viewModel.isLoading }
    val isSearching by remember { viewModel.isSearching }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        val itemCount = if (pokemonList.size % 2 == 0) {
            pokemonList.size / 2
        } else {
            pokemonList.size / 2 + 1
        }

        items(itemCount) {
            if (it >= itemCount - 1 && !endReached && !isLoading && !isSearching) {
                LaunchedEffect(key1 = true) {
                    viewModel.loadPokemonPaginated()
                }
            }
            PokeDoxRow(rowIndex = it, entries = pokemonList, navController = navController)
        }

    }
}

@Composable
fun PokeDexEntry(
    entry: PokeDexListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(modifier = modifier
        .shadow(5.dp, RoundedCornerShape(10.dp))
        .clip(RoundedCornerShape(10.dp))
        .aspectRatio(1f)
        .background(
            Brush.verticalGradient(
                listOf(
                    dominantColor, defaultDominantColor
                )
            )
        )
        .clickable {
            navController.navigate(
                "pokemon_detail_screen/${dominantColor.toArgb()}/${entry.pokemonName}",
            )
        }) {
        Column {
            AsyncImage(
                model = entry.imageUrl, contentDescription = entry.pokemonName, onSuccess = {
                    viewModel.calcDominantColor(it.result.drawable) { color ->
                        dominantColor = color
                    }
                }, modifier = Modifier.weight(9f), contentScale = ContentScale.FillBounds
            )
            Text(
                text = entry.pokemonName,
                fontSize = 20.sp,
                fontFamily = RobotoCondensed,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f)
            )
        }
    }
}

@Composable
fun PokeDoxRow(
    rowIndex: Int, entries: List<PokeDexListEntry>, navController: NavController
) {
    Column {
        Row {
            PokeDexEntry(
                entry = entries[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (entries.size >= rowIndex * 2 + 2) {
                PokeDexEntry(
                    entry = entries[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

}

