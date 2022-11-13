package uz.mahmudovmurodil.pokedox.util

import androidx.compose.ui.graphics.Color
import uz.mahmudovmurodil.pokedox.data.remote.responses.Stat
import uz.mahmudovmurodil.pokedox.data.remote.responses.Type
import uz.mahmudovmurodil.pokedox.ui.theme.*
import java.util.*

fun parseTypeToColor(type: Type): Color {
    return when (type.type.name.lowercase(Locale.ROOT)) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        "bug" -> TypeBug
        else -> {
            Color.Black
        }
    }
}

fun parseStatToColor(stat: Stat): Color {
    return when (stat.stat.name.lowercase()) {
        "hp" -> HPColor
        "attack" -> AtkColor
        "defence" -> DefColor
        "special-attack" -> SpAtkColor
        "special-defense" -> SpDefColor
        "speed" -> SpdColor
        else -> {
            Color.White
        }
    }
}

fun parseStatToAbbr(stat: Stat): String {
    return when (stat.stat.name.lowercase()) {
        "hp" -> "HP"
        "attack" -> "Atk"
        "defence" -> "Def"
        "special-attack" -> "SpAtk"
        "special-defense" -> "SpDef"
        "speed" -> "Spd"
        else -> {
            ""
        }
    }
}