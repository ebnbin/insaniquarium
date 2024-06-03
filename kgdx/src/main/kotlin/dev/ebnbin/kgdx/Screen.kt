package dev.ebnbin.kgdx

import dev.ebnbin.kgdx.asset.Asset

class Screen(
    val assetSet: Set<Asset<*>>,
    val stageList: List<LifecycleStage>,
) {
    class Creator(
        val assetSet: Set<Asset<*>>,
        val createStageList: () -> List<LifecycleStage>,
    )
}
