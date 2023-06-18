import dev.ebnbin.gdx.utils.AnimMode
import dev.ebnbin.gdx.utils.Interpolation
import dev.ebnbin.insaniquarium.body.BodyConfig
import dev.ebnbin.insaniquarium.body.BodyType

object ConfigInfo {
    val bodyList: List<BodyConfigInfo> = listOf(
        BodyConfigInfo(
            type = BodyType.CLYDE,
            group = BodyConfig.Group.PET,
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
            anim = BodyConfig.Anim(
                assetId = "clyde",
                duration = 1.125f,
                mode = AnimMode.LOOP,
                interpolation = Interpolation.POW2_IN,
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
            eatAct = BodyConfig.EatAct(
                foodTypeSet = setOf(
                    BodyType.SILVER_COIN,
                    BodyType.GOLD_COIN,
                    BodyType.STAR,
                    BodyType.DIAMOND,
                    BodyType.TREASURE_CHEST,
                ),
                accelerationX = 0.2f,
                accelerationY = 0.3f,
            ),
        ),
        BodyConfigInfo(
            type = BodyType.PRESTO,
            group = BodyConfig.Group.PET,
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
            anim = BodyConfig.Anim(
                assetId = "presto",
                duration = 0.5f,
                mode = AnimMode.LOOP,
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
            turnAct = BodyConfig.TurnAct(
                anim = BodyConfig.Anim(
                    type = BodyConfig.AnimType.TURN,
                    assetId = "presto_turn",
                    duration = 0.5f,
                    mode = AnimMode.NORMAL,
                ),
            ),
        ),
        BodyConfigInfo(
            type = BodyType.SILVER_COIN,
            group = BodyConfig.Group.MONEY,
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
            disappearAct = BodyConfig.DisappearAct(),
        ),
        BodyConfigInfo(
            type = BodyType.GOLD_COIN,
            group = BodyConfig.Group.MONEY,
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
            disappearAct = BodyConfig.DisappearAct(),
        ),
        BodyConfigInfo(
            type = BodyType.STAR,
            group = BodyConfig.Group.MONEY,
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
            disappearAct = BodyConfig.DisappearAct(),
        ),
        BodyConfigInfo(
            type = BodyType.DIAMOND,
            group = BodyConfig.Group.MONEY,
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
            disappearAct = BodyConfig.DisappearAct(),
        ),
        BodyConfigInfo(
            type = BodyType.TREASURE_CHEST,
            group = BodyConfig.Group.MONEY,
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
            disappearAct = BodyConfig.DisappearAct(),
        ),
        BodyConfigInfo(
            type = BodyType.BEETLE,
            group = BodyConfig.Group.MONEY,
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
            disappearAct = BodyConfig.DisappearAct(),
        ),
    )
}
