import dev.ebnbin.gdx.utils.fromJson
import dev.ebnbin.insaniquarium.body.BodyConfig
import dev.ebnbin.insaniquarium.body.BodyConfigInitializer
import dev.ebnbin.insaniquarium.body.BodyType
import java.io.File

object ConfigInfo {
    fun bodyMap(): Map<BodyType, BodyConfig> {
        BodyConfigInitializer.assets = File("../assets/assets.json").readText().fromJson()
        return BodyConfigInitializer.bodyMap()
    }
}
