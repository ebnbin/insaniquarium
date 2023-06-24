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
                BodyConfig.AnimationType.SWIM to TextureRegionAnimation(
                    assetId = "starcatcher",
                    duration = 1f,
                    mode = AnimationMode.LOOP,
                ),
                BodyConfig.AnimationType.HUNGRY_SWIM to TextureRegionAnimation(
                    assetId = "starcatcher_hungry",
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
                foods = mapOf(
                    BodyType.STAR to BodyConfig.Food(
                        damagePerSecond = 10f,
                        hunger = 20f,
                    ),
                ),
                accelerationX = 0.2f,
                accelerationY = 0f,
                fullHunger = 20f,
                maxHungerPercent = 1.5f,
                hungryHungerPercent = 0.5f,
                hungerRatePerSecond = 1f,
                canDie = true,
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
                BodyConfig.AnimationType.SWIM to TextureRegionAnimation(
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
                foods = mapOf(
                    BodyType.SILVER_COIN to BodyConfig.Food(
                        damagePerSecond = 10f,
                        hunger = 0f,
                    ),
                    BodyType.STAR to BodyConfig.Food(
                        damagePerSecond = 10f,
                        hunger = 0f,
                    ),
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
                BodyConfig.AnimationType.SWIM to TextureRegionAnimation(
                    assetId = "gash",
                    duration = 0.5f,
                    mode = AnimationMode.LOOP,
                ),
                BodyConfig.AnimationType.TURN to TextureRegionAnimation(
                    assetId = "gash_turn",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                BodyConfig.AnimationType.EAT to TextureRegionAnimation(
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
                foods = mapOf(
                    BodyType.BEETLE to BodyConfig.Food(
                        damagePerSecond = 10f,
                        hunger = 20f,
                    ),
                ),
                accelerationX = 0.8f,
                accelerationY = 0.5f,
                hasAnimation = true,
                fullHunger = 10f,
                maxHungerPercent = 3f,
                hungerRatePerSecond = 1f,
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
                BodyConfig.AnimationType.SWIM to TextureRegionAnimation(
                    assetId = "presto",
                    duration = 0.5f,
                    mode = AnimationMode.LOOP,
                ),
                BodyConfig.AnimationType.TURN to TextureRegionAnimation(
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
            canDisappear = true,
            health = 1f,
            animations = mapOf(
                BodyConfig.AnimationType.SWIM to TextureRegionAnimation(
                    assetId = "silver_coin",
                    duration = 0.6f,
                    mode = AnimationMode.LOOP,
                ),
            ),
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
            canDisappear = true,
            health = 1f,
            animations = mapOf(
                BodyConfig.AnimationType.SWIM to TextureRegionAnimation(
                    assetId = "star",
                    duration = 0.6f,
                    mode = AnimationMode.LOOP,
                ),
            ),
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
            canDisappear = true,
            health = 10f,
            animations = mapOf(
                BodyConfig.AnimationType.SWIM to TextureRegionAnimation(
                    assetId = "beetle",
                    duration = 0.3f,
                    mode = AnimationMode.LOOP,
                ),
            ),
        ),
    )
}
