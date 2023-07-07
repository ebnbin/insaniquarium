data class AquariumTextureInfo(
    val name: String,
    val srcFileName: String,
)

data class BodyTextureInfo(
    val group: String,
    val srcFileName: String,
    val srcMaskFileName: String,
    val scale: Float,
    val row: Int,
    val column: Int,
    val outputList: List<Output>,
) {
    data class Output(
        val name: String,
        val tileStart: Int,
        val tileCount: Int,
        val startIndex: Int = 0,
    )
}
