data class AquariumTextureInfo(
    val name: String,
    val srcFileName: String,
)

data class PetTextureInfo(
    val name: String,
    val srcFileName: String,
    val srcMaskFileName: String,
    val scale: Float,
    val row: Int,
    val column: Int,
)
