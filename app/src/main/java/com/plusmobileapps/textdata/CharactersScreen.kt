package com.plusmobileapps.textdata

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.plusmobileapps.text.TextData

data class CharacterUiModel(
    val id: String,
    val name: TextData,
    val description: TextData,
    val imageUrl: String,
)

sealed class CharactersUiState {
    data object Loading : CharactersUiState()
    data class Loaded(val characters: List<CharacterUiModel>) : CharactersUiState()
    data class Error(val textData: TextData) : CharactersUiState()
}

@Composable
fun CharactersScreen(
    modifier: Modifier = Modifier,
    viewModel: CharactersViewModel = viewModel()
) {
    val state = viewModel.state.collectAsState()
    CharactersContent(state = state.value)
}

@Composable
private fun CharactersContent(
    modifier: Modifier = Modifier,
    state: CharactersUiState,
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        val contentModifier = Modifier.padding(paddingValues)
        when (state) {
            is CharactersUiState.Error -> Text(state.textData.evaluate())
            is CharactersUiState.Loaded -> CharactersLoadedContent(
                modifier = contentModifier,
                characters = state.characters,
            )

            CharactersUiState.Loading -> LoadingContent(contentModifier)
        }
    }
}

@Composable
fun CharactersLoadedContent(
    modifier: Modifier = Modifier,
    characters: List<CharacterUiModel>,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
    ) {
        items(characters.size) { index ->
            val character = characters[index]
            CharacterItem(
                modifier = Modifier.padding(8.dp),
                character = character,
            )
        }
    }
}

@Composable
fun CharacterItem(
    modifier: Modifier = Modifier,
    character: CharacterUiModel,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = {}
    ) {
        Column {
            AsyncImage(
                model = character.imageUrl,
                contentDescription = null,
            )
            Text(character.name.evaluate())
            Text(character.description.evaluate())
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}