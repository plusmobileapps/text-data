package com.plusmobileapps.textdata

import com.plusmobileapps.rickandmorty.api.RickAndMortyApi
import com.plusmobileapps.rickandmorty.api.RickAndMortyApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class CharactersRepository(
    private val api: RickAndMortyApiClient = RickAndMortyApi.instance,
    private val ioContext: CoroutineContext = Dispatchers.IO
) {

    private val _state = MutableStateFlow<State>(State.Idle)

    val state: StateFlow<State> = _state.asStateFlow()

    suspend fun fetchCharacters(page: Int = 0): Unit = withContext(ioContext) {
        _state.value = State.Loading
        try {
            val response = api.getCharacters(page = page)
            _state.value = State.Success(
                characters = response.results.map { Character.fromApiModel(it) }
            )
        } catch (e: Exception) {
            _state.value = State.Error(e)
        }
    }

    sealed class State {
        data class Success(val characters: List<Character>) : State()
        data class Error(val exception: Exception) : State()
        data object Loading : State()
        data object Idle : State()
    }

}