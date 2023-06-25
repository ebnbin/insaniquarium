import dev.ebnbin.gdx.animation.TextureRegionAnimation
import dev.ebnbin.gdx.animation.AnimationMode
import dev.ebnbin.gdx.utils.Interpolation
import dev.ebnbin.insaniquarium.body.BodyConfig
import dev.ebnbin.insaniquarium.body.BodyType

object ConfigInfo {
    val bodyList: List<BodyConfigInfo> = listOf(
        BodyConfigInfo(
            type = BodyType.STAR_POTION,
            group = BodyConfig.Group.FOOD,
            width = BodyConfigInfo.Size(
                textureName = "star_potion",
                index = 0,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "star_potion",
                index = 0,
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "star_potion",
                index = 0,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            density = 1020f,
            health = 1f,
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "star_potion",
                    duration = 1f,
                    mode = AnimationMode.LOOP,
                ),
            ),
            eatAct = BodyConfig.EatAct(
                foods = emptyMap(),
                accelerationX = 0f,
                accelerationY = 0f,
                canDie = true,
            )
        ),
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
            corpseDensity = 1020f,
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "starcatcher",
                    duration = 1f,
                    mode = AnimationMode.LOOP,
                ),
                hungry = TextureRegionAnimation(
                    assetId = "starcatcher_hungry",
                    duration = 1f,
                    mode = AnimationMode.LOOP,
                ),
                die = TextureRegionAnimation(
                    assetId = "starcatcher_die",
                    duration = 1f,
                    mode = AnimationMode.NORMAL,
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
            type = BodyType.BEETLEMUNCHER,
            group = BodyConfig.Group.FISH,
            width = BodyConfigInfo.Size(
                textureName = "beetlemuncher",
                index = 0,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "beetlemuncher",
                index = 0,
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "beetlemuncher_turn",
                index = 5,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            corpseDensity = 1020f,
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "beetlemuncher",
                    duration = 0.5f,
                    mode = AnimationMode.LOOP,
                ),
                turn = TextureRegionAnimation(
                    assetId = "beetlemuncher_turn",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                eat = TextureRegionAnimation(
                    assetId = "beetlemuncher_eat",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                hungry = TextureRegionAnimation(
                    assetId = "beetlemuncher_hungry",
                    duration = 0.5f,
                    mode = AnimationMode.LOOP,
                ),
                hungryTurn = TextureRegionAnimation(
                    assetId = "beetlemuncher_hungry_turn",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                hungryEat = TextureRegionAnimation(
                    assetId = "beetlemuncher_hungry_eat",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                die = TextureRegionAnimation(
                    assetId = "beetlemuncher_die",
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
                    BodyType.STAR_POTION to BodyConfig.Food(
                        damagePerSecond = 10f,
                        hunger = 20f,
                    ),
                ),
                accelerationX = 0.4f,
                accelerationY = 0.25f,
                hasAnimation = true,
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
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
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
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "gash",
                    duration = 0.5f,
                    mode = AnimationMode.LOOP,
                ),
                turn = TextureRegionAnimation(
                    assetId = "gash_turn",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                eat = TextureRegionAnimation(
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
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "presto",
                    duration = 0.5f,
                    mode = AnimationMode.LOOP,
                ),
                turn = TextureRegionAnimation(
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
            health = 1f,
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "silver_coin",
                    duration = 0.6f,
                    mode = AnimationMode.LOOP,
                ),
            ),
            eatAct = BodyConfig.EatAct(
                foods = emptyMap(),
                accelerationX = 0f,
                accelerationY = 0f,
                canDie = true,
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
            health = 1f,
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "star",
                    duration = 0.6f,
                    mode = AnimationMode.LOOP,
                ),
            ),
            eatAct = BodyConfig.EatAct(
                foods = emptyMap(),
                accelerationX = 0f,
                accelerationY = 0f,
                canDie = true,
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
            health = 10f,
            animations = BodyConfig.Animations(
                swim =  TextureRegionAnimation(
                    assetId = "beetle",
                    duration = 0.3f,
                    mode = AnimationMode.LOOP,
                ),
            ),
            eatAct = BodyConfig.EatAct(
                foods = emptyMap(),
                accelerationX = 0f,
                accelerationY = 0f,
                canDie = true,
            )
        ),
    )
}
