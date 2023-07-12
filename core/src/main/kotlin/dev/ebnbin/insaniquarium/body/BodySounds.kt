package dev.ebnbin.insaniquarium.body

import dev.ebnbin.gdx.asset.Asset
import dev.ebnbin.gdx.lifecycle.baseGame

data class BodySounds(
    val init: String? = null,
    val eat: List<String>? = null,
    val die: List<String>? = null,
) {
    fun allAssets(): Set<Asset<*>> {
        val set = mutableSetOf<String?>()
        set.add(init)
        eat?.let { set.addAll(it) }
        die?.let { set.addAll(it) }
        return set.filterNotNull().mapTo(mutableSetOf()) { baseGame.assets.sound.getValue(it) }
    }
}
