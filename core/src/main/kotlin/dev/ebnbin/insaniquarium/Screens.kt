package dev.ebnbin.insaniquarium

import dev.ebnbin.insaniquarium.aquarium.Aquarium
import dev.ebnbin.insaniquarium.aquarium.AquariumStage
import dev.ebnbin.kgdx.Screen

object Screens {
    fun aquarium(aquarium: Aquarium): Screen.Creator {
        return Screen.Creator(
            assetSet = setOf(aquarium.textureAsset),
            createStageList = {
                listOf(AquariumStage(aquarium))
            },
        )
    }
}
