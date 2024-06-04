package dev.ebnbin.insaniquarium

import dev.ebnbin.insaniquarium.aquarium.Aquarium
import dev.ebnbin.insaniquarium.aquarium.AquariumStage
import dev.ebnbin.kgdx.Screen

object Screens {
    private fun aquarium(aquarium: Aquarium): Screen.Creator {
        return Screen.Creator(
            name = "aquarium_${aquarium.id}",
            assetSet = setOf(aquarium.textureAsset),
            createStageList = {
                listOf(AquariumStage(aquarium))
            },
        )
    }

    val ALL: List<Screen.Creator> = mutableListOf<Screen.Creator>().also { list ->
        Aquarium.entries.forEach { aquarium ->
            list.add(aquarium(aquarium))
        }
    }
}
