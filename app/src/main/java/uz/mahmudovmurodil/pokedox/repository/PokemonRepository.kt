package uz.mahmudovmurodil.pokedox.repository

import dagger.hilt.android.scopes.ActivityScoped
import uz.mahmudovmurodil.pokedox.data.remote.PokeApi
import uz.mahmudovmurodil.pokedox.data.remote.responses.Pokemon
import uz.mahmudovmurodil.pokedox.data.remote.responses.PokemonList
import uz.mahmudovmurodil.pokedox.util.Resource
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(
    private val api: PokeApi
) {

    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        val response = try {
            api.getPokemonList(limit, offset)
        } catch (e: Exception) {
            return Resource.Error(message = "An unknown error occured.")
        }
        return Resource.Success(response)
    }

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        val response = try {
            api.getPokemonInfo(pokemonName)
        } catch (e: Exception) {
            return Resource.Error(message = "An unknown error occured.")
        }
        return Resource.Success(response)
    }
}