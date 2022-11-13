package uz.mahmudovmurodil.pokedox.pokemonlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.mahmudovmurodil.pokedox.data.models.PokeDexListEntry
import uz.mahmudovmurodil.pokedox.repository.PokemonRepository
import uz.mahmudovmurodil.pokedox.util.Constants.PAGE_SIZE
import uz.mahmudovmurodil.pokedox.util.Resource
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var curPage = 0

    var pokemonList = mutableStateOf<List<PokeDexListEntry>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cashedPokemonList = listOf<PokeDexListEntry>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    init {
        loadPokemonPaginated()
    }

    fun searchPokemon(query: String) {
        val listToSearch = if (isSearchStarting) {
            pokemonList.value
        } else {
            cashedPokemonList
        }

        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                pokemonList.value = cashedPokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val result = listToSearch.filter {
                it.pokemonName.contains(query.trim(), ignoreCase = true) ||
                        it.number.toString().contains(query.trim())
            }
            if (isSearchStarting) {
                cashedPokemonList = pokemonList.value
                isSearchStarting = false
            }
            pokemonList.value = result
            isSearching.value = true
        }
    }

    fun loadPokemonPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)
            when (result) {
                is Resource.Success -> {
                    endReached.value = curPage * PAGE_SIZE >= result.data!!.count
                    val pokedoxEnrties = result.data.results.mapIndexed { index, entry ->
                        val number = if (entry.url.endsWith("/")) {
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.url.takeLastWhile { it.isDigit() }
                        }
                        val url =
                            "https://raw.githubusercontent.com/PokeApi/sprites/master/sprites/pokemon/${number}.png"
                        PokeDexListEntry(entry.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        }, url, number.toInt())
                    }

                    loadError.value = ""
                    isLoading.value = false
                    pokemonList.value += pokedoxEnrties
                    curPage++
                }
                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }
            }
        }
    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate() { palatte ->
            palatte?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }

        }
    }
}