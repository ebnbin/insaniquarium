package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.utils.SerializableEnum

enum class BodyGroup(override val serializedName: String) : SerializableEnum {
    FOOD("food"),
    FISH("fish"),
    PET("pet"),
    MONEY("money"),
    ;
}
