import dev.ebnbin.gdx.utils.World
import dev.ebnbin.insaniquarium.body.BodyConfig
import dev.ebnbin.insaniquarium.body.BodyType

data class AquariumTextureInfo(
    val name: String,
    val srcFileName: String,
)

data class BodyTextureInfo(
    val group: String,
    val srcFileName: String,
    val srcMaskFileName: String,
    val scale: Float,
    val row: Int,
    val column: Int,
    val outputList: List<Output>,
) {
    data class Output(
        val name: String,
        val tileStart: Int,
        val tileCount: Int,
        val startIndex: Int = 0,
    )
}

data class BodyConfigInfo(
    val type: BodyType,
    val group: BodyConfig.Group,
    val width: Size,
    val height: Size,
    val depth: Size,
    val density: Float = World.DENSITY_WATER,
    val dragCoefficient: Float = World.DEFAULT_DRAG_COEFFICIENT,
    val health: Float = BodyConfig.HEALTH_MAX,
    val hunger: BodyConfig.Hunger,
    val animations: BodyConfig.Animations,
    val touchAct: BodyConfig.TouchAct? = null,
    val swimActX: BodyConfig.SwimAct? = null,
    val swimActY: BodyConfig.SwimAct? = null,
    val eatAct: BodyConfig.EatAct? = null,
) {
    data class Size(
        val textureName: String,
        val index: Int? = null,
        val wh: WH,
    )

    enum class WH {
        WIDTH,
        HEIGHT,
        ;
    }
}
