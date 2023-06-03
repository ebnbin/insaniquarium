package dev.ebnbin.gdx.lifecycle

import dev.ebnbin.gdx.asset.Asset

class Screen(
    val assetSet: Set<Asset<*>>,
    val stageList: List<BaseStage>,
)
