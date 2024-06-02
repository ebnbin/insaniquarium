package dev.ebnbin.kgdx.asset

import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.loaders.AssetLoader

interface AssetLoaderRegistry {
    fun <T, P : AssetLoaderParameters<T>> setLoader(type: Class<T>, suffix: String?, loader: AssetLoader<T, P>)
}
