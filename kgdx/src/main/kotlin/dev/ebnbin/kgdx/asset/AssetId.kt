package dev.ebnbin.kgdx.asset

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import dev.ebnbin.kgdx.util.internalAsset
import dev.ebnbin.kgdx.util.localAsset

data class AssetId(
    val fileType: AssetFileType,
    val type: AssetType,
    val nameWithExtension: String,
) {
    val id: String = "${fileType.id}:${type.id}:$nameWithExtension"

    fun fileHandle(): FileHandle {
        val path = "${type.directory}/$nameWithExtension"
        return when (fileType) {
            AssetFileType.INTERNAL -> Gdx.files.internalAsset(path)
            AssetFileType.LOCAL -> Gdx.files.localAsset(path)
        }
    }

    companion object {
        fun of(id: String): AssetId {
            val (fileType, type, nameWithExtension) = id.split(":")
            return AssetId(
                fileType = AssetFileType.of(fileType),
                type = AssetType.of(type),
                nameWithExtension = nameWithExtension,
            )
        }
    }
}
