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

    private const val GROUP_FOOD = "food"
    private const val GROUP_FISH = "fish"
    private const val GROUP_ALIEN = "alien"
    private const val GROUP_PET = "pet"
    private const val GROUP_MONEY = "money"

    val bodyList: List<BodyTextureInfo> = listOf(
        BodyTextureInfo(
            group = GROUP_FOOD,
            srcFileName = "food.gif",
            srcMaskFileName = "_food.gif",
            scale = 1.5f,
            row = 5,
            column = 10,
            outputList = listOf(
                BodyTextureInfo.Output(
                    name = "fish_food",
                    tileStart = 0,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "fish_food_zorf",
                    tileStart = 10,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "star_potion",
                    tileStart = 30,
                    tileCount = 10,
                ),
            ),
        ),
        BodyTextureInfo(
            group = GROUP_FISH,
            srcFileName = "smallswim.gif",
            srcMaskFileName = "smallswim_.gif",
            scale = 1.5f,
            row = 5,
            column = 10,
            outputList = listOf(
                BodyTextureInfo.Output(
                    name = "guppy_baby",
                    tileStart = 0,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_medium",
                    tileStart = 10,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_large",
                    tileStart = 20,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_king",
                    tileStart = 30,
                    tileCount = 10,
                ),
            ),
        ),
        BodyTextureInfo(
            group = GROUP_FISH,
            srcFileName = "smallturn.gif",
            srcMaskFileName = "smallturn_.gif",
            scale = 1.5f,
            row = 5,
            column = 10,
            outputList = listOf(
                BodyTextureInfo.Output(
                    name = "guppy_baby_turn",
                    tileStart = 0,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_medium_turn",
                    tileStart = 10,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_large_turn",
                    tileStart = 20,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_king_turn",
                    tileStart = 30,
                    tileCount = 10,
                ),
            ),
        ),
        BodyTextureInfo(
            group = GROUP_FISH,
            srcFileName = "smalleat.gif",
            srcMaskFileName = "smalleat_.gif",
            scale = 1.5f,
            row = 5,
            column = 10,
            outputList = listOf(
                BodyTextureInfo.Output(
                    name = "guppy_baby_eat",
                    tileStart = 0,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_medium_eat",
                    tileStart = 10,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_large_eat",
                    tileStart = 20,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_king_eat",
                    tileStart = 30,
                    tileCount = 10,
                ),
            ),
        ),
        BodyTextureInfo(
            group = GROUP_FISH,
            srcFileName = "hungryswim.gif",
            srcMaskFileName = "_hungryswim.gif",
            scale = 1.5f,
            row = 5,
            column = 10,
            outputList = listOf(
                BodyTextureInfo.Output(
                    name = "guppy_baby_hungry",
                    tileStart = 0,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_medium_hungry",
                    tileStart = 10,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_large_hungry",
                    tileStart = 20,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_king_hungry",
                    tileStart = 30,
                    tileCount = 10,
                ),
            ),
        ),
        BodyTextureInfo(
            group = GROUP_FISH,
            srcFileName = "hungryturn.gif",
            srcMaskFileName = "_hungryturn.gif",
            scale = 1.5f,
            row = 5,
            column = 10,
            outputList = listOf(
                BodyTextureInfo.Output(
                    name = "guppy_baby_hungry_turn",
                    tileStart = 0,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_medium_hungry_turn",
                    tileStart = 10,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_large_hungry_turn",
                    tileStart = 20,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_king_hungry_turn",
                    tileStart = 30,
                    tileCount = 10,
                ),
            ),
        ),
        BodyTextureInfo(
            group = GROUP_FISH,
            srcFileName = "hungryeat.gif",
            srcMaskFileName = "hungryeat_.gif",
            scale = 1.5f,
            row = 5,
            column = 10,
            outputList = listOf(
                BodyTextureInfo.Output(
                    name = "guppy_baby_hungry_eat",
                    tileStart = 0,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_medium_hungry_eat",
                    tileStart = 10,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_large_hungry_eat",
                    tileStart = 20,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_king_hungry_eat",
                    tileStart = 30,
                    tileCount = 10,
                ),
            ),
        ),
        BodyTextureInfo(
            group = GROUP_FISH,
            srcFileName = "smalldie.gif",
            srcMaskFileName = "_smalldie.gif",
            scale = 1.5f,
            row = 5,
            column = 10,
            outputList = listOf(
                BodyTextureInfo.Output(
                    name = "guppy_baby_die",
                    tileStart = 0,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_medium_die",
                    tileStart = 10,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_large_die",
                    tileStart = 20,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "guppy_king_die",
                    tileStart = 30,
                    tileCount = 10,
                ),
            ),
        ),
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
                BodyTextureInfo.Output(
                    name = "starcatcher_die",
                    tileStart = 20,
                    tileCount = 10,
                ),
            ),
        ),
        BodyTextureInfo(
            group = GROUP_FISH,
            srcFileName = "gekko.gif",
            srcMaskFileName = "_gekko.gif",
            scale = 1.5f,
            row = 7,
            column = 10,
            outputList = listOf(
                BodyTextureInfo.Output(
                    name = "beetlemuncher",
                    tileStart = 0,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "beetlemuncher_turn",
                    tileStart = 10,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "beetlemuncher_eat",
                    tileStart = 20,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "beetlemuncher_hungry",
                    tileStart = 30,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "beetlemuncher_hungry_turn",
                    tileStart = 40,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "beetlemuncher_hungry_eat",
                    tileStart = 60,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "beetlemuncher_die",
                    tileStart = 50,
                    tileCount = 10,
                ),
            ),
        ),
        BodyTextureInfo(
            group = GROUP_ALIEN,
            srcFileName = "sylv.gif",
            srcMaskFileName = "_sylv.gif",
            scale = 1.5f,
            row = 2,
            column = 10,
            outputList = listOf(
                BodyTextureInfo.Output(
                    name = "sylvester",
                    tileStart = 0,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "sylvester_turn",
                    tileStart = 10,
                    tileCount = 10,
                ),
            ),
        ),
        BodyTextureInfo(
            group = GROUP_PET,
            srcFileName = "niko.gif",
            srcMaskFileName = "_niko.gif",
            scale = 1.5f,
            row = 3,
            column = 10,
            outputList = listOf(
                BodyTextureInfo.Output(
                    name = "niko",
                    tileStart = 0,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "niko_charge",
                    tileStart = 10,
                    tileCount = 9,
                ),
                BodyTextureInfo.Output(
                    name = "niko_charged",
                    tileStart = 19,
                    tileCount = 1,
                ),
                BodyTextureInfo.Output(
                    name = "niko_hungry_charge",
                    tileStart = 20,
                    tileCount = 9,
                ),
                BodyTextureInfo.Output(
                    name = "niko_hungry_charged",
                    tileStart = 29,
                    tileCount = 1,
                ),
            ),
        ),
        BodyTextureInfo(
            group = GROUP_PET,
            srcFileName = "prego.gif",
            srcMaskFileName = "_prego.gif",
            scale = 1.5f,
            row = 4,
            column = 10,
            outputList = listOf(
                BodyTextureInfo.Output(
                    name = "prego",
                    tileStart = 0,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "prego_charged",
                    tileStart = 10,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "prego_turn",
                    tileStart = 20,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "prego_charge",
                    tileStart = 30,
                    tileCount = 6,
                ),
                BodyTextureInfo.Output(
                    name = "prego_discharge",
                    tileStart = 36,
                    tileCount = 4,
                ),
            ),
        ),
        BodyTextureInfo(
            group = GROUP_PET,
            srcFileName = "zorf.gif",
            srcMaskFileName = "_zorf.gif",
            scale = 1.5f,
            row = 3,
            column = 10,
            outputList = listOf(
                BodyTextureInfo.Output(
                    name = "zorf",
                    tileStart = 0,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "zorf_turn",
                    tileStart = 10,
                    tileCount = 10,
                ),
                BodyTextureInfo.Output(
                    name = "zorf_drop",
                    tileStart = 20,
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
                    name = "beetle",
                    tileStart = 50,
                    tileCount = 10,
                ),
            ),
        ),
    )
}
