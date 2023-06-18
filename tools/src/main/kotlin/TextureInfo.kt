import java.io.File

object TextureInfo {
    val dstDir: File = File("../assets/texture")

    fun srcDir(group: String): File {
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

    private const val GROUP_FISH = "fish"
    private const val GROUP_PET = "pet"
    private const val GROUP_MONEY = "money"

    val bodyList: List<BodyTextureInfo> = listOf(
        BodyTextureInfo(
            group = GROUP_FISH,
            srcFileName = "starcatcher.gif",
            srcMaskFileName = "_starcatcher.gif",
            scale = 1.5f,
            row = 3,
            column = 10,
            outputList = listOf(
                BodyTextureInfo.Output(
                    name = "starcatcher",
                    tileStart = 0,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "starcatcher_hungry",
                    tileStart = 10,
                    tileCount = 10,
                ),
            ),
        ),
        BodyTextureInfo(
            group = GROUP_PET,
            srcFileName = "clyde.gif",
            srcMaskFileName = "_clyde.gif",
            scale = 1.5f,
            row = 1,
            column = 10,
            outputList = listOf(
                BodyTextureInfo.Output(
                    name = "clyde",
                    tileStart = 0,
                    tileCount = 10,
                    startIndex = 4,
                ),
            ),
        ),
        BodyTextureInfo(
            group = GROUP_PET,
            srcFileName = "gash.gif",
            srcMaskFileName = "_gash.gif",
            scale = 1.5f,
            row = 3,
            column = 10,
            outputList = listOf(
                BodyTextureInfo.Output(
                    name = "gash",
                    tileStart = 0,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "gash_turn",
                    tileStart = 10,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "gash_eat",
                    tileStart = 20,
                    tileCount = 10,
                ),
            ),
        ),
        BodyTextureInfo(
            group = GROUP_PET,
            srcFileName = "presto.gif",
            srcMaskFileName = "_presto.gif",
            scale = 1.5f,
            row = 3,
            column = 10,
            outputList = listOf(
                BodyTextureInfo.Output(
                    name = "presto",
                    tileStart = 0,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "presto_turn",
                    tileStart = 10,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "presto_transform",
                    tileStart = 20,
                    tileCount = 10,
                ),
            ),
        ),
        BodyTextureInfo(
            group = GROUP_MONEY,
            srcFileName = "money.gif",
            srcMaskFileName = "money_.gif",
            scale = 1.5f,
            row = 6,
            column = 10,
            outputList = listOf(
                BodyTextureInfo.Output(
                    name = "silver_coin",
                    tileStart = 0,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "gold_coin",
                    tileStart = 10,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "star",
                    tileStart = 20,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "diamond",
                    tileStart = 30,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "treasure_chest",
                    tileStart = 40,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "beetle",
                    tileStart = 50,
                    tileCount = 10,
                ),
            ),
        ),
    )
}
