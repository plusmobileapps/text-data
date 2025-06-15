package com.plusmobileapps.textdata

import com.plusmobileapps.rickandmorty.api.characters.CharacterDTO

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val imageUrl: String,
) {
    companion object {
        fun fromApiModel(apiModel: CharacterDTO): Character = Character(
            id = apiModel.id,
            name = apiModel.name,
            status = apiModel.status.name,
            species = apiModel.species,
            type = apiModel.type,
            imageUrl = apiModel.image,
        )
    }
}