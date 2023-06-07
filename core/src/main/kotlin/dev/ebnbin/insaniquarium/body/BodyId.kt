package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.SerializableEnum

enum class BodyId(override val serializedName: String) : SerializableEnum {
    CLYDE("clyde"),
    PRESTO("presto"),

    SILVER_COIN("silver_coin"),
    GOLD_COIN("gold_coin"),
    STAR("star"),
    DIAMOND("diamond"),
    TREASURE_CHEST("treasure_chest"),
    BEETLE("beetle"),
    ;
}
