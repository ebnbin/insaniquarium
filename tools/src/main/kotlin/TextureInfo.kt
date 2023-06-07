import java.io.File

object TextureInfo {
    val dstDir: File = File("../assets/texture")

    private fun srcDir(group: String): File {
        return File("files/images/$group")
    }

    //*****************************************************************************************************************

    val aquariumSrcDir: File = srcDir("aquarium")

    val aquariumList: List<AquariumTextureInfo> = listOf(
        AquariumTextureInfo(
            name = "aquarium_a",
            srcFileName = "aquarium1.jpg",
        ),
        AquariumTextureInfo(
            name = "aquarium_b",
            srcFileName = "aquarium4.jpg",
        ),
        AquariumTextureInfo(
            name = "aquarium_c",
            srcFileName = "aquarium3.jpg",
        ),
        AquariumTextureInfo(
            name = "aquarium_d",
            srcFileName = "aquarium2.jpg",
        ),
        AquariumTextureInfo(
            name = "aquarium_e",
            srcFileName = "aquarium6.jpg",
        ),
        AquariumTextureInfo(
            name = "aquarium_f",
            srcFileName = "Aquarium5.jpg",
        ),
    )

    //*****************************************************************************************************************

    val petSrcDir: File = srcDir("pet")

    val petList: List<PetTextureInfo> = listOf(
        PetTextureInfo(
            srcFileName = "clyde.gif",
            srcMaskFileName = "_clyde.gif",
            scale = 1.5f,
            row = 1,
            column = 10,
            outputList = listOf(
                PetTextureInfo.Output(
                    name = "clyde",
                    tileStart = 0,
                    tileCount = 10,
                    startIndex = 4,
                ),
            ),
        ),
        PetTextureInfo(
            srcFileName = "presto.gif",
            srcMaskFileName = "_presto.gif",
            scale = 1.5f,
            row = 3,
            column = 10,
            outputList = listOf(
                PetTextureInfo.Output(
                    name = "presto",
                    tileStart = 0,
                    tileCount = 10,
                ),
                PetTextureInfo.Output(
                    name = "presto_turn",
                    tileStart = 10,
                    tileCount = 10,
                ),
                PetTextureInfo.Output(
                    name = "presto_transform",
                    tileStart = 20,
                    tileCount = 10,
                ),
            ),
        ),
    )
}
