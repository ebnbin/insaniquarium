package dev.ebnbin.insaniquarium

import dev.ebnbin.insaniquarium.aquarium.AquariumType
import dev.ebnbin.insaniquarium.aquarium.AquariumStage
import dev.ebnbin.insaniquarium.body.BodyType
import dev.ebnbin.insaniquarium.tank.TankStage
import dev.ebnbin.kgdx.Screen
import dev.ebnbin.kgdx.asset.Asset

object Screens {
    private fun aquarium(aquariumType: AquariumType): Screen.Creator {
        return Screen.Creator(
            name = aquariumType.id,
            assetSet = mutableSetOf<Asset<*>>().also { set ->
                set.add(aquariumType.textureAsset)
                set.addAll(BodyType.entries.map { it.def.textureAsset })
            },
            createStageList = {
                listOf(
                    AquariumStage(aquariumType),
                    TankStage(),
                )
            },
        )
    }

    val ALL: List<Screen.Creator> = mutableListOf<Screen.Creator>().also { list ->
        AquariumType.entries.forEach { aquarium ->
            list.add(aquarium(aquarium))
        }
    }
}
