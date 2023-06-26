package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.SerializableEnum

enum class BodyType(override val serializedName: String) : SerializableEnum {
    STAR_POTION("star_potion"),

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
