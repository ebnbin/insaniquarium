package dev.ebnbin.insaniquarium.tool.asset

import dev.ebnbin.insaniquarium.body.BodyDef
import dev.ebnbin.insaniquarium.body.BodyDefJsonWrapper
import dev.ebnbin.insaniquarium.body.BodyType
import dev.ebnbin.kgdx.asset.AssetFileType
import dev.ebnbin.kgdx.asset.JsonAsset
import dev.ebnbin.kgdx.util.toJson
import java.io.File

object BodyDefProcessor {
    fun process(): Pair<String, JsonAsset> {
        val bodyDefJsonWrapper = BodyDefJsonWrapper(
            data = BodyType.entries.associate { type ->
                val def = BodyDef(
                    id = type.id,
                )
                def.id to def
            },
        )
        val file = File("../assets/assets/json/body_def.json")
        file.parentFile?.mkdirs()
        file.writeText(bodyDefJsonWrapper.toJson())
        val jsonAsset = JsonAsset(
            name = "body_def",
            extension = "json",
            fileType = AssetFileType.INTERNAL,
            preload = true,
            classOfT = BodyDefJsonWrapper::class.java,
        )
        return jsonAsset.name to jsonAsset
    }
}
