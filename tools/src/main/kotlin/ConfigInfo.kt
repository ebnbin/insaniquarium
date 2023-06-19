import dev.ebnbin.gdx.animation.TextureRegionAnimation
import dev.ebnbin.gdx.animation.AnimationMode
import dev.ebnbin.gdx.utils.Interpolation
import dev.ebnbin.insaniquarium.body.BodyConfig
import dev.ebnbin.insaniquarium.body.BodyType

object ConfigInfo {
    val bodyList: List<BodyConfigInfo> = listOf(
        BodyConfigInfo(
            type = BodyType.STARCATCHER,
            group = BodyConfig.Group.FISH,
            width = BodyConfigInfo.Size(
                textureName = "starcatcher",
                index = 0,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "starcatcher",
                index = 0,
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "starcatcher",
                index = 0,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            density = 1080f,
            animations = mapOf(
                BodyConfig.AnimationType.SWIM.serializedName to TextureRegionAnimation(
                    assetId = "starcatcher",
                    duration = 1f,
                    mode = AnimationMode.LOOP,
                ),
            ),
            swimActX = BodyConfig.SwimAct(
                acceleration = 0.1f,
                idlingTimeRandomStart = 2f,
                idlingTimeRandomEnd = 8f,
            ),
            eatAct = BodyConfig.EatAct(
                foodTypeSet = setOf(
                    BodyType.STAR,
                ),
                accelerationX = 0.2f,
                accelerationY = 0f,
            ),
        ),
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
            animations = mapOf(
                BodyConfig.AnimationType.SWIM.serializedName to TextureRegionAnimation(
                    assetId = "clyde",
                    duration = 1.125f,
                    mode = AnimationMode.LOOP,
                    interpolation = Interpolation.POW2_IN,
                ),
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
            type = BodyType.GASH,
            group = BodyConfig.Group.PET,
            width = BodyConfigInfo.Size(
                textureName = "gash",
                index = 0,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "gash",
                index = 0,
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "gash_turn",
                index = 5,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            animations = mapOf(
                BodyConfig.AnimationType.SWIM.serializedName to TextureRegionAnimation(
                    assetId = "gash",
                    duration = 0.5f,
                    mode = AnimationMode.LOOP,
                ),
                BodyConfig.AnimationType.TURN.serializedName to TextureRegionAnimation(
                    assetId = "gash_turn",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                BodyConfig.AnimationType.EAT.serializedName to TextureRegionAnimation(
                    assetId = "gash_eat",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
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
            eatAct = BodyConfig.EatAct(
                foodTypeSet = setOf(
                    BodyType.BEETLE,
                ),
                accelerationX = 0.8f,
                accelerationY = 0.5f,
                hasAnimation = true,
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
            animations = mapOf(
                BodyConfig.AnimationType.SWIM.serializedName to TextureRegionAnimation(
                    assetId = "presto",
                    duration = 0.5f,
                    mode = AnimationMode.LOOP,
                ),
                BodyConfig.AnimationType.TURN.serializedName to TextureRegionAnimation(
                    assetId = "presto_turn",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
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
            animations = mapOf(
                BodyConfig.AnimationType.SWIM.serializedName to TextureRegionAnimation(
                    assetId = "silver_coin",
                    duration = 0.6f,
                    mode = AnimationMode.LOOP,
                ),
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
            animations = mapOf(
                BodyConfig.AnimationType.SWIM.serializedName to TextureRegionAnimation(
                    assetId = "gold_coin",
                    duration = 0.6f,
                    mode = AnimationMode.LOOP,
                ),
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
            animations = mapOf(
                BodyConfig.AnimationType.SWIM.serializedName to TextureRegionAnimation(
                    assetId = "star",
                    duration = 0.6f,
                    mode = AnimationMode.LOOP,
                ),
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
            animations = mapOf(
                BodyConfig.AnimationType.SWIM.serializedName to TextureRegionAnimation(
                    assetId = "diamond",
                    duration = 0.6f,
                    mode = AnimationMode.LOOP,
                ),
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
            animations = mapOf(
                BodyConfig.AnimationType.SWIM.serializedName to TextureRegionAnimation(
                    assetId = "treasure_chest",
                    duration = 0.6f,
                    mode = AnimationMode.LOOP,
                ),
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
            animations = mapOf(
                BodyConfig.AnimationType.SWIM.serializedName to TextureRegionAnimation(
                    assetId = "beetle",
                    duration = 0.3f,
                    mode = AnimationMode.LOOP,
                ),
            ),
            disappearAct = BodyConfig.DisappearAct(),
        ),
    )
}
