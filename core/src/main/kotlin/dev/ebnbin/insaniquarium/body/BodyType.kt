package dev.ebnbin.insaniquarium.body

import dev.ebnbin.kgdx.game

enum class BodyType(val id: String) {
    STINKY("stinky"),
    NIKO("niko"),
    ITCHY("itchy"),
    PREGO("prego"),
    ZORF("zorf"),
    CLYDE("clyde"),
    VERT("vert"),
    RUFUS("rufus"),
    MERYL("meryl"),
    WADSWORTH("wadsworth"),
    SEYMOUR("seymour"),
    SHRAPNEL("shrapnel"),
    GUMBO("gumbo"),
    BLIP("blip"),
    RHUBARB("rhubarb"),
    NIMBUS("nimbus"),
    AMP("amp"),
    GASH("gash"),
    ANGIE("angie"),
    PRESTO("presto"),
    BRINKLEY("brinkley"),
    NOSTRADAMUS("nostradamus"),
    STANLEY("stanley"),
    WALTER("walter"),
    ;

    val def: BodyDef
        get() = game.assets.json("body_def").data<Map<String, BodyDef>>().getValue(id)

    companion object {
        val PET_LIST = listOf(
            STINKY,
            NIKO,
            ITCHY,
            PREGO,
            ZORF,
            CLYDE,
            VERT,
            RUFUS,
            MERYL,
            WADSWORTH,
            SEYMOUR,
            SHRAPNEL,
            GUMBO,
            BLIP,
            RHUBARB,
            NIMBUS,
            AMP,
            GASH,
            ANGIE,
            PRESTO,
            BRINKLEY,
            NOSTRADAMUS,
            STANLEY,
            WALTER,
        )
    }
}
