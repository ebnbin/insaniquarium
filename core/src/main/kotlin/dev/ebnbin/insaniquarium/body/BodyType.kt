package dev.ebnbin.insaniquarium.body

import dev.ebnbin.kgdx.game

enum class BodyType(
    val id: String,
) {
    STINKY("stinky"),
    ;

    val def: BodyDef
        get() = game.assets.json("body_def").data<Map<String, BodyDef>>().getValue(id)
}
