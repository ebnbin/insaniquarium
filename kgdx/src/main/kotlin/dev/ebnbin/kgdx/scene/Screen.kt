package dev.ebnbin.kgdx.scene

import dev.ebnbin.kgdx.asset.Asset

class Screen(
    val name: String,
    val assetSet: Set<Asset<*>>,
    val sceneList: List<LifecycleScene>,
) {
    class Creator(
        val name: String,
        val assetSet: Set<Asset<*>>,
        val createSceneList: () -> List<LifecycleScene>,
    ) {
        companion object {
            val EMPTY: Creator = Creator(
                name = "empty",
                assetSet = emptySet(),
                createSceneList = { emptyList() },
            )
        }
    }
}
