package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.SerializableEnum

enum class BodyType(override val serializedName: String) : SerializableEnum {
    STARCATCHER("starcatcher"),

    CLYDE("clyde"),
    GASH("gash"),
    PRESTO("presto"),

    SILVER_COIN("silver_coin"),
    STAR("star"),
    BEETLE("beetle"),
    ;

    companion object {
        fun of(value: String): BodyType {
            return values().single { it.serializedName == value }
        }
    }
}
