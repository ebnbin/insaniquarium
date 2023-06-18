package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.SerializableEnum

enum class BodyType(override val serializedName: String) : SerializableEnum {
    STARCATCHER("starcatcher"),

    CLYDE("clyde"),
    GASH("gash"),
    PRESTO("presto"),

    SILVER_COIN("silver_coin"),
    GOLD_COIN("gold_coin"),
    STAR("star"),
    DIAMOND("diamond"),
    TREASURE_CHEST("treasure_chest"),
    BEETLE("beetle"),
    ;
}
