package uz.mahmudovmurodil.pokedox.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.mahmudovmurodil.pokedox.data.remote.PokeApi
import uz.mahmudovmurodil.pokedox.repository.PokemonRepository
import uz.mahmudovmurodil.pokedox.util.Constants.BASE_URL
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePokemonRepository(
        api: PokeApi
    ) = PokemonRepository(api = api)

    @Singleton
    @Provides
    fun providePOkeApi(): PokeApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PokeApi::class.java)
    }
}