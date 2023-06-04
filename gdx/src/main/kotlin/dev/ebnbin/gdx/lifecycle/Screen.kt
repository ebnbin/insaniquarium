package dev.ebnbin.gdx.lifecycle

import com.kotcrab.vis.ui.widget.Menu
import dev.ebnbin.gdx.asset.Asset

class Screen(
    val assetSet: Set<Asset<*>>,
    val stageList: List<BaseStage>,
    val devMenu: Menu?,
)
