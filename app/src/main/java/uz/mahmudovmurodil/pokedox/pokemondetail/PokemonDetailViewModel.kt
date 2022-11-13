package uz.mahmudovmurodil.pokedox.pokemondetail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uz.mahmudovmurodil.pokedox.data.remote.responses.Pokemon
import uz.mahmudovmurodil.pokedox.repository.PokemonRepository
import uz.mahmudovmurodil.pokedox.util.Resource
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        return repository.getPokemonInfo(pokemonName)
    }
}