package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.SerializableEnum

enum class BodyType(override val serializedName: String) : SerializableEnum {
    FISH_FOOD("fish_food"),
    STAR_POTION("star_potion"),

    GUPPY_SMALL("guppy_small"),
    GUPPY_MEDIUM("guppy_medium"),
    GUPPY_LARGE("guppy_large"),
    GUPPY_KING("guppy_king"),
    STARCATCHER("starcatcher"),
    BEETLEMUNCHER("beetlemuncher"),

    CLYDE("clyde"),
    GASH("gash"),
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
            STAR_POTION,
        )
        val FISH_LIST: List<BodyType> = listOf(
            GUPPY_SMALL,
            GUPPY_MEDIUM,
            GUPPY_LARGE,
            GUPPY_KING,
            STARCATCHER,
            BEETLEMUNCHER,
        )
        val PET_LIST: List<BodyType> = listOf(
            CLYDE,
            GASH,
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
