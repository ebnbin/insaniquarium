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
        ),
        BodyConfigInfo(
            id = "presto",
            width = BodyConfigInfo.Size(
                textureName = "presto",
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "presto",
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "presto_turn",
                index = 5,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            touchAct = BodyConfig.TouchAct(
                accelerationX = 0.4f,
                accelerationY = 0.25f,
            ),
            swimActX = BodyConfig.SwimAct(
                acceleration = 0.2f,
                idlingTimeRandomStart = 2f,
                idlingTimeRandomEnd = 8f,
            ),
            swimActY = BodyConfig.SwimAct(
                acceleration = 0.125f,
                idlingTimeRandomStart = 6f,
                idlingTimeRandomEnd = 10f,
            ),
            anim = BodyConfig.Anim(
                assetId = "presto",
                duration = 0.5f,
                mode = AnimMode.LOOP,
            ),
        ),
        BodyConfigInfo(
            id = "silver_coin",
            width = BodyConfigInfo.Size(
                textureName = "silver_coin",
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "silver_coin",
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "silver_coin",
                index = 5,
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            density = 1020f,
            anim = BodyConfig.Anim(
                assetId = "silver_coin",
                duration = 0.6f,
                mode = AnimMode.LOOP,
            ),
        ),
        BodyConfigInfo(
            id = "gold_coin",
            width = BodyConfigInfo.Size(
                textureName = "gold_coin",
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "gold_coin",
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "gold_coin",
                index = 5,
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            density = 1020f,
            anim = BodyConfig.Anim(
                assetId = "gold_coin",
                duration = 0.6f,
                mode = AnimMode.LOOP,
            ),
        ),
        BodyConfigInfo(
            id = "star",
            width = BodyConfigInfo.Size(
                textureName = "star",
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "star",
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "star",
                index = 5,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            density = 1010f,
            anim = BodyConfig.Anim(
                assetId = "star",
                duration = 0.6f,
                mode = AnimMode.LOOP,
            ),
        ),
        BodyConfigInfo(
            id = "diamond",
            width = BodyConfigInfo.Size(
                textureName = "diamond",
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "diamond",
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "diamond",
                index = 5,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            density = 1020f,
            anim = BodyConfig.Anim(
                assetId = "diamond",
                duration = 0.6f,
                mode = AnimMode.LOOP,
            ),
        ),
        BodyConfigInfo(
            id = "treasure_chest",
            width = BodyConfigInfo.Size(
                textureName = "treasure_chest",
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "treasure_chest",
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "treasure_chest",
                index = 5,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            density = 1020f,
            anim = BodyConfig.Anim(
                assetId = "treasure_chest",
                duration = 0.6f,
                mode = AnimMode.LOOP,
            ),
        ),
        BodyConfigInfo(
            id = "beetle",
            width = BodyConfigInfo.Size(
                textureName = "beetle",
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "beetle",
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "beetle",
                index = 5,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            density = 990f,
            anim = BodyConfig.Anim(
                assetId = "beetle",
                duration = 0.3f,
                mode = AnimMode.LOOP,
            ),
        ),
    )
}
