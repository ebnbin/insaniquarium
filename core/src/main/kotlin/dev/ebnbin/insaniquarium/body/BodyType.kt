package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.SerializableEnum

enum class BodyType(override val serializedName: String) : SerializableEnum {
    FISH_FOOD("fish_food"),
    FISH_FOOD_ZORF("fish_food_zorf"),
    STAR_POTION("star_potion"),

    GUPPY_BABY("guppy_baby"),
    GUPPY_MEDIUM("guppy_medium"),
    GUPPY_LARGE("guppy_large"),
    GUPPY_KING("guppy_king"),
    STARCATCHER("starcatcher"),
    BEETLEMUNCHER("beetlemuncher"),
    GUPPY_BABY_CORPSE("guppy_baby_corpse"),
    GUPPY_MEDIUM_CORPSE("guppy_medium_corpse"),
    GUPPY_LARGE_CORPSE("guppy_large_corpse"),
    GUPPY_KING_CORPSE("guppy_king_corpse"),
    STARCATCHER_CORPSE("starcatcher_corpse"),
    BEETLEMUNCHER_CORPSE("beetlemuncher_corpse"),

    SYLVESTER("sylvester"),

    NIKO("niko"),
    PREGO("prego"),
    ZORF("zorf"),
    CLYDE("clyde"),
    GASH("gash"),
    ANGIE("angie"),
    PRESTO("presto"),

    SILVER_COIN("silver_coin"),
    GOLD_COIN("gold_coin"),
    STAR("star"),
    DIAMOND("diamond"),
    BEETLE("beetle"),
    ;

    companion object {
        val FOOD_LIST: List<BodyType> = listOf(
            FISH_FOOD,
            FISH_FOOD_ZORF,
            STAR_POTION,
        )
        val FISH_LIST: List<BodyType> = listOf(
            GUPPY_BABY,
            GUPPY_MEDIUM,
            GUPPY_LARGE,
            GUPPY_KING,
            STARCATCHER,
            BEETLEMUNCHER,
        )
        val FISH_CORPSE_LIST: List<BodyType> = listOf(
            GUPPY_BABY_CORPSE,
            GUPPY_MEDIUM_CORPSE,
            GUPPY_LARGE_CORPSE,
            GUPPY_KING_CORPSE,
            STARCATCHER_CORPSE,
            BEETLEMUNCHER_CORPSE,
        )
        val ALIEN_LIST: List<BodyType> = listOf(
            SYLVESTER,
        )
        val PET_LIST: List<BodyType> = listOf(
            NIKO,
            PREGO,
            ZORF,
            CLYDE,
            GASH,
            ANGIE,
            PRESTO,
        )
        val MONEY_LIST: List<BodyType> = listOf(
            SILVER_COIN,
            GOLD_COIN,
            DIAMOND,
            STAR,
            BEETLE,
        )
    }
}
