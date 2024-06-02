package dev.ebnbin.kgdx.util

import com.badlogic.gdx.Files
import com.badlogic.gdx.files.FileHandle

fun Files.internalAsset(path: String): FileHandle {
    return internal("assets/$path")
}

fun Files.localAsset(path: String): FileHandle {
    return local("assets/$path")
}
