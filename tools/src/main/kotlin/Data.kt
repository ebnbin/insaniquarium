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

data class MusicInfo(
    val name: String,
    val srcFileName: String,
) {
    companion object {
        val all: List<MusicInfo> = listOf(
            MusicInfo(
                name = "aquarium_a",
                srcFileName = "Insaniq2.mo3(1).wav",
            ),
            MusicInfo(
                name = "aquarium_b",
                srcFileName = "Insaniq2.mo3(3).wav",
            ),
            MusicInfo(
                name = "aquarium_c",
                srcFileName = "Insaniq2.mo3(7).wav",
            ),
            MusicInfo(
                name = "aquarium_d",
                srcFileName = "Insaniq2.mo3(2).wav",
            ),
            MusicInfo(
                name = "aquarium_e",
                srcFileName = "Lullaby.mo3.wav",
            ),
            MusicInfo(
                name = "aquarium_f",
                srcFileName = "Insaniq2.mo3(4).wav",
            ),
        )
    }
}

data class SoundInfo(
    val name: String,
    val srcFileName: String,
) {
    companion object {
        val all: List<SoundInfo> = listOf(
            SoundInfo(
                name = "drop_food",
                srcFileName = "DROPFOOD.ogg",
            )
        )
    }
}
