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
    STAR("star"),
    BEETLE("beetle"),
    ;
}
