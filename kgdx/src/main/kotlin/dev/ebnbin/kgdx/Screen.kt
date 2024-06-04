package dev.ebnbin.kgdx

import dev.ebnbin.kgdx.asset.Asset

class Screen(
    val name: String,
    val assetSet: Set<Asset<*>>,
    val stageList: List<LifecycleStage>,
) {
    class Creator(
        val name: String,
        val assetSet: Set<Asset<*>>,
        val createStageList: () -> List<LifecycleStage>,
    ) {
        companion object {
            val EMPTY: Creator = Creator(
                name = "empty",
                assetSet = emptySet(),
                createStageList = { emptyList() },
            )
        }
    }
}
