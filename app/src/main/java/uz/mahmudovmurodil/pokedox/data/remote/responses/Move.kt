package uz.mahmudovmurodil.pokedox.data.remote.responses

data class Move(
    val move: MoveX,
    val version_group_details: List<VersionGroupDetail>
)