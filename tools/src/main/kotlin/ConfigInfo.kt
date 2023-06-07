import dev.ebnbin.gdx.utils.AnimMode
import dev.ebnbin.gdx.utils.Interpolation
import dev.ebnbin.insaniquarium.body.BodyConfig

object ConfigInfo {
    val bodyList: List<BodyConfigInfo> = listOf(
        BodyConfigInfo(
            id = "clyde",
            width = BodyConfigInfo.Size(
                textureName = "clyde",
                index = 0,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "clyde",
                index = 0,
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "clyde",
                index = 0,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            touchAct = BodyConfig.TouchAct(
                accelerationX = 0.2f,
                accelerationY = 0.3f,
            ),
            swimActX = BodyConfig.SwimAct(
                acceleration = 0.1f,
                idlingTimeRandomStart = 2f,
                idlingTimeRandomEnd = 8f,
            ),
            swimActY = BodyConfig.SwimAct(
                acceleration = 0.15f,
                idlingTimeRandomStart = 2f,
                idlingTimeRandomEnd = 8f,
            ),
            anim = BodyConfig.Anim(
                assetId = "clyde",
                duration = 1.125f,
                mode = AnimMode.LOOP,
                interpolation = Interpolation.POW2_IN,
            ),
        )
    )
}
