package dev.ebnbin.insaniquarium

import dev.ebnbin.insaniquarium.aquarium.Aquarium
import dev.ebnbin.insaniquarium.aquarium.AquariumStage
import dev.ebnbin.insaniquarium.body.BodyType
import dev.ebnbin.insaniquarium.tank.TankStage
import dev.ebnbin.kgdx.Screen
import dev.ebnbin.kgdx.asset.Asset
import dev.ebnbin.kgdx.game

object Screens {
    private fun aquarium(aquarium: Aquarium): Screen.Creator {
        return Screen.Creator(
            name = "aquarium_${aquarium.id}",
            assetSet = mutableSetOf<Asset<*>>().also { set ->
                set.add(game.assets.json("body_def"))
                set.add(aquarium.textureAsset)
                set.addAll(BodyType.entries.map { it.textureAsset })
            },
            createStageList = {
                listOf(
                    AquariumStage(aquarium),
                    TankStage(),
                )
            },
        )
    }

    val ALL: List<Screen.Creator> = mutableListOf<Screen.Creator>().also { list ->
        Aquarium.entries.forEach { aquarium ->
            list.add(aquarium(aquarium))
        }
    }
}
