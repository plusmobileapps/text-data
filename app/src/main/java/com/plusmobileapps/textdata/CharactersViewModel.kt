package com.plusmobileapps.textdata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plusmobileapps.text.FixedString
import com.plusmobileapps.text.LocaleTextData
import com.plusmobileapps.text.PhraseModel
import com.plusmobileapps.text.ResourceString
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

class CharactersViewModel(
    private val repository: CharactersRepository = CharactersRepository()
) : ViewModel() {

    val state: StateFlow<CharactersUiState> = repository.state
        .map { state ->
            mapToUiState(state)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = CharactersUiState.Loading,
        )

    init {
        fetchCharacters()
    }

    fun fetchCharacters(page: Int = 0) {
        viewModelScope.launch {
            repository.fetchCharacters(page)
        }
    }

    fun refresh() {
        fetchCharacters()
    }

    private fun mapToUiState(state: CharactersRepository.State) =
        when (state) {
            is CharactersRepository.State.Error -> {
                CharactersUiState.Error(ResourceString(R.string.character_error))
            }

            CharactersRepository.State.Idle,
            CharactersRepository.State.Loading -> CharactersUiState.Loading

            is CharactersRepository.State.Success -> CharactersUiState.Loaded(
                characters = state.characters.map { character ->
                    CharacterUiModel(
                        id = character.id.toString(),
                        name = PhraseModel(
                            R.string.character_name,
                            "name" to FixedString(character.name),
                        ),
                        description = LocaleTextData(
                            Locale("es"),  // Spanish locale
                            ResourceString(R.string.character_error)
                        ),
                        imageUrl = character.imageUrl
                    )
                }
            )
        }
}