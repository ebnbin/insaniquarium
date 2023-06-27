import dev.ebnbin.gdx.animation.TextureRegionAnimation
import dev.ebnbin.gdx.animation.AnimationMode
import dev.ebnbin.gdx.utils.Interpolation
import dev.ebnbin.insaniquarium.body.BodyConfig
import dev.ebnbin.insaniquarium.body.BodyGroup
import dev.ebnbin.insaniquarium.body.BodyType

object ConfigInfo {
    val bodyList: List<BodyConfigInfo> = listOf(
        BodyConfigInfo(
            type = BodyType.FISH_FOOD,
            group = BodyGroup.FOOD,
            width = BodyConfigInfo.Size(
                textureName = "fish_food",
                index = 0,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "fish_food",
                index = 0,
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "fish_food",
                index = 0,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            density = 1020f,
            isDead = true,
            health = BodyConfig.Health(
                full = 1f,
            ),
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "fish_food",
                    duration = 1f,
                    mode = AnimationMode.LOOP,
                ),
            ),
        ),
        BodyConfigInfo(
            type = BodyType.STAR_POTION,
            group = BodyGroup.FOOD,
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
            isDead = true,
            health = BodyConfig.Health(
                full = 1f,
            ),
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "star_potion",
                    duration = 1f,
                    mode = AnimationMode.LOOP,
                ),
            ),
        ),
        BodyConfigInfo(
            type = BodyType.GUPPY_SMALL,
            group = BodyGroup.FISH,
            width = BodyConfigInfo.Size(
                textureName = "guppy_small",
                index = 0,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "guppy_small",
                index = 0,
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "guppy_small_turn",
                index = 5,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            health = BodyConfig.Health(
                full = 1f,
            ),
            hunger = BodyConfig.Hunger(
                full = 20f,
                maxThreshold = 1.5f,
                hungryThreshold = 0.5f,
                diffPerSecond = -1f,
                transformation = BodyType.GUPPY_SMALL_CORPSE,
            ),
            growth = BodyConfig.Growth(
                full = 10f,
                transformation = BodyType.GUPPY_MEDIUM,
            ),
            eatAct = BodyConfig.EatAct(
                foods = mapOf(
                    BodyType.FISH_FOOD to BodyConfig.Food(
                        healthDiffPerSecond = -10f,
                        hunger = 20f,
                        growth = 5f,
                    ),
                ),
                drivingAccelerationX = 0.4f,
                drivingAccelerationY = 0.25f,
            ),
            swimActX = BodyConfig.SwimAct(
                drivingAcceleration = 0.2f,
                idlingTimeRandomStart = 2f,
                idlingTimeRandomEnd = 8f,
            ),
            swimActY = BodyConfig.SwimAct(
                drivingAcceleration = 0.125f,
                idlingTimeRandomStart = 6f,
                idlingTimeRandomEnd = 10f,
            ),
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "guppy_small",
                    duration = 0.5f,
                    mode = AnimationMode.LOOP,
                ),
                turn = TextureRegionAnimation(
                    assetId = "guppy_small_turn",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                eat = TextureRegionAnimation(
                    assetId = "guppy_small_eat",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                hungry = TextureRegionAnimation(
                    assetId = "guppy_small_hungry",
                    duration = 0.5f,
                    mode = AnimationMode.LOOP,
                ),
                hungryTurn = TextureRegionAnimation(
                    assetId = "guppy_small_hungry_turn",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                hungryEat = TextureRegionAnimation(
                    assetId = "guppy_small_hungry_eat",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                die = TextureRegionAnimation(
                    assetId = "guppy_small_die",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
            ),
        ),
        BodyConfigInfo(
            type = BodyType.GUPPY_MEDIUM,
            group = BodyGroup.FISH,
            width = BodyConfigInfo.Size(
                textureName = "guppy_medium",
                index = 0,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "guppy_medium",
                index = 0,
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "guppy_medium_turn",
                index = 5,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            health = BodyConfig.Health(
                full = 1f,
            ),
            hunger = BodyConfig.Hunger(
                full = 20f,
                maxThreshold = 1.5f,
                hungryThreshold = 0.5f,
                diffPerSecond = -1f,
                canDie = true,
                corpseDensity = 1020f,
            ),
            growth = BodyConfig.Growth(
                full = 15f,
                transformation = BodyType.GUPPY_LARGE,
            ),
            drop = BodyConfig.Drop(
                full = 20f,
                diffPerSecond = 3f,
                production = BodyType.SILVER_COIN,
            ),
            eatAct = BodyConfig.EatAct(
                foods = mapOf(
                    BodyType.FISH_FOOD to BodyConfig.Food(
                        healthDiffPerSecond = -10f,
                        hunger = 20f,
                        growth = 5f,
                    ),
                ),
                drivingAccelerationX = 0.4f,
                drivingAccelerationY = 0.25f,
            ),
            swimActX = BodyConfig.SwimAct(
                drivingAcceleration = 0.2f,
                idlingTimeRandomStart = 2f,
                idlingTimeRandomEnd = 8f,
            ),
            swimActY = BodyConfig.SwimAct(
                drivingAcceleration = 0.125f,
                idlingTimeRandomStart = 6f,
                idlingTimeRandomEnd = 10f,
            ),
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "guppy_medium",
                    duration = 0.5f,
                    mode = AnimationMode.LOOP,
                ),
                turn = TextureRegionAnimation(
                    assetId = "guppy_medium_turn",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                eat = TextureRegionAnimation(
                    assetId = "guppy_medium_eat",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                hungry = TextureRegionAnimation(
                    assetId = "guppy_medium_hungry",
                    duration = 0.5f,
                    mode = AnimationMode.LOOP,
                ),
                hungryTurn = TextureRegionAnimation(
                    assetId = "guppy_medium_hungry_turn",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                hungryEat = TextureRegionAnimation(
                    assetId = "guppy_medium_hungry_eat",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                die = TextureRegionAnimation(
                    assetId = "guppy_medium_die",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
            ),
        ),
        BodyConfigInfo(
            type = BodyType.GUPPY_LARGE,
            group = BodyGroup.FISH,
            width = BodyConfigInfo.Size(
                textureName = "guppy_large",
                index = 0,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "guppy_large",
                index = 0,
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "guppy_large_turn",
                index = 5,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            health = BodyConfig.Health(
                full = 1f,
            ),
            hunger = BodyConfig.Hunger(
                full = 20f,
                maxThreshold = 1.5f,
                hungryThreshold = 0.5f,
                diffPerSecond = -1f,
                canDie = true,
                corpseDensity = 1020f,
            ),
            growth = BodyConfig.Growth(
                full = 25f,
                transformation = BodyType.GUPPY_KING,
            ),
            drop = BodyConfig.Drop(
                full = 20f,
                diffPerSecond = 3f,
                production = BodyType.GOLD_COIN,
            ),
            eatAct = BodyConfig.EatAct(
                foods = mapOf(
                    BodyType.FISH_FOOD to BodyConfig.Food(
                        healthDiffPerSecond = -10f,
                        hunger = 20f,
                        growth = 5f,
                    ),
                ),
                drivingAccelerationX = 0.4f,
                drivingAccelerationY = 0.25f,
            ),
            swimActX = BodyConfig.SwimAct(
                drivingAcceleration = 0.2f,
                idlingTimeRandomStart = 2f,
                idlingTimeRandomEnd = 8f,
            ),
            swimActY = BodyConfig.SwimAct(
                drivingAcceleration = 0.125f,
                idlingTimeRandomStart = 6f,
                idlingTimeRandomEnd = 10f,
            ),
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "guppy_large",
                    duration = 0.5f,
                    mode = AnimationMode.LOOP,
                ),
                turn = TextureRegionAnimation(
                    assetId = "guppy_large_turn",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                eat = TextureRegionAnimation(
                    assetId = "guppy_large_eat",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                hungry = TextureRegionAnimation(
                    assetId = "guppy_large_hungry",
                    duration = 0.5f,
                    mode = AnimationMode.LOOP,
                ),
                hungryTurn = TextureRegionAnimation(
                    assetId = "guppy_large_hungry_turn",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                hungryEat = TextureRegionAnimation(
                    assetId = "guppy_large_hungry_eat",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                die = TextureRegionAnimation(
                    assetId = "guppy_large_die",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
            ),
        ),
        BodyConfigInfo(
            type = BodyType.GUPPY_KING,
            group = BodyGroup.FISH,
            width = BodyConfigInfo.Size(
                textureName = "guppy_king",
                index = 0,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "guppy_king",
                index = 0,
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "guppy_king_turn",
                index = 5,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            health = BodyConfig.Health(
                full = 1f,
            ),
            hunger = BodyConfig.Hunger(
                full = 20f,
                maxThreshold = 1.5f,
                hungryThreshold = 0.5f,
                diffPerSecond = -1f,
                canDie = true,
                corpseDensity = 1020f,
            ),
            drop = BodyConfig.Drop(
                full = 20f,
                diffPerSecond = 3f,
                production = BodyType.DIAMOND,
            ),
            eatAct = BodyConfig.EatAct(
                foods = mapOf(
                    BodyType.FISH_FOOD to BodyConfig.Food(
                        healthDiffPerSecond = -10f,
                        hunger = 20f,
                    ),
                ),
                drivingAccelerationX = 0.4f,
                drivingAccelerationY = 0.25f,
            ),
            swimActX = BodyConfig.SwimAct(
                drivingAcceleration = 0.2f,
                idlingTimeRandomStart = 2f,
                idlingTimeRandomEnd = 8f,
            ),
            swimActY = BodyConfig.SwimAct(
                drivingAcceleration = 0.125f,
                idlingTimeRandomStart = 6f,
                idlingTimeRandomEnd = 10f,
            ),
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "guppy_king",
                    duration = 0.5f,
                    mode = AnimationMode.LOOP,
                ),
                turn = TextureRegionAnimation(
                    assetId = "guppy_king_turn",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                eat = TextureRegionAnimation(
                    assetId = "guppy_king_eat",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                hungry = TextureRegionAnimation(
                    assetId = "guppy_king_hungry",
                    duration = 0.5f,
                    mode = AnimationMode.LOOP,
                ),
                hungryTurn = TextureRegionAnimation(
                    assetId = "guppy_king_hungry_turn",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                hungryEat = TextureRegionAnimation(
                    assetId = "guppy_king_hungry_eat",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
                die = TextureRegionAnimation(
                    assetId = "guppy_king_die",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
            ),
        ),
        BodyConfigInfo(
            type = BodyType.STARCATCHER,
            group = BodyGroup.FISH,
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
            hunger = BodyConfig.Hunger(
                full = 20f,
                maxThreshold = 1.5f,
                hungryThreshold = 0.5f,
                diffPerSecond = -1f,
                canDie = true,
                corpseDensity = 1020f,
            ),
            drop = BodyConfig.Drop(
                full = 1f,
                production = BodyType.BEETLE,
            ),
            eatAct = BodyConfig.EatAct(
                foods = mapOf(
                    BodyType.STAR to BodyConfig.Food(
                        healthDiffPerSecond = -10f,
                        hunger = 20f,
                        drop = 1f,
                    ),
                ),
                drivingAccelerationX = 0.2f,
                drivingAccelerationY = 0f,
            ),
            swimActX = BodyConfig.SwimAct(
                drivingAcceleration = 0.1f,
                idlingTimeRandomStart = 2f,
                idlingTimeRandomEnd = 8f,
            ),
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
        ),
        BodyConfigInfo(
            type = BodyType.BEETLEMUNCHER,
            group = BodyGroup.FISH,
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
            hunger = BodyConfig.Hunger(
                full = 20f,
                maxThreshold = 1.5f,
                hungryThreshold = 0.5f,
                diffPerSecond = -1f,
                canDie = true,
                corpseDensity = 1020f,
            ),
            eatAct = BodyConfig.EatAct(
                foods = mapOf(
                    BodyType.STAR_POTION to BodyConfig.Food(
                        healthDiffPerSecond = -10f,
                        hunger = 20f,
                    ),
                ),
                drivingAccelerationX = 0.4f,
                drivingAccelerationY = 0.25f,
            ),
            swimActX = BodyConfig.SwimAct(
                drivingAcceleration = 0.2f,
                idlingTimeRandomStart = 2f,
                idlingTimeRandomEnd = 8f,
            ),
            swimActY = BodyConfig.SwimAct(
                drivingAcceleration = 0.125f,
                idlingTimeRandomStart = 6f,
                idlingTimeRandomEnd = 10f,
            ),
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
        ),
        BodyConfigInfo(
            type = BodyType.GUPPY_SMALL_CORPSE,
            group = BodyGroup.FISH,
            width = BodyConfigInfo.Size(
                textureName = "guppy_small",
                index = 0,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "guppy_small",
                index = 0,
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "guppy_small_turn",
                index = 5,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            density = 1020f,
            isDead = true,
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "guppy_small_die",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
            ),
        ),
        BodyConfigInfo(
            type = BodyType.GUPPY_MEDIUM_CORPSE,
            group = BodyGroup.FISH,
            width = BodyConfigInfo.Size(
                textureName = "guppy_medium",
                index = 0,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "guppy_medium",
                index = 0,
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "guppy_medium_turn",
                index = 5,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            density = 1020f,
            isDead = true,
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "guppy_medium_die",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
            ),
        ),
        BodyConfigInfo(
            type = BodyType.GUPPY_LARGE_CORPSE,
            group = BodyGroup.FISH,
            width = BodyConfigInfo.Size(
                textureName = "guppy_large",
                index = 0,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "guppy_large",
                index = 0,
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "guppy_large_turn",
                index = 5,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            density = 1020f,
            isDead = true,
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "guppy_large_die",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
            ),
        ),
        BodyConfigInfo(
            type = BodyType.GUPPY_KING_CORPSE,
            group = BodyGroup.FISH,
            width = BodyConfigInfo.Size(
                textureName = "guppy_king",
                index = 0,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            height = BodyConfigInfo.Size(
                textureName = "guppy_king",
                index = 0,
                wh = BodyConfigInfo.WH.HEIGHT,
            ),
            depth = BodyConfigInfo.Size(
                textureName = "guppy_king_turn",
                index = 5,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            density = 1020f,
            isDead = true,
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "guppy_king_die",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
            ),
        ),
        BodyConfigInfo(
            type = BodyType.STARCATCHER_CORPSE,
            group = BodyGroup.FISH,
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
            density = 1020f,
            isDead = true,
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "starcatcher_die",
                    duration = 1f,
                    mode = AnimationMode.NORMAL,
                ),
            ),
        ),
        BodyConfigInfo(
            type = BodyType.BEETLEMUNCHER_CORPSE,
            group = BodyGroup.FISH,
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
            density = 1020f,
            isDead = true,
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "beetlemuncher_die",
                    duration = 0.5f,
                    mode = AnimationMode.NORMAL,
                ),
            ),
        ),
        BodyConfigInfo(
            type = BodyType.CLYDE,
            group = BodyGroup.PET,
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
            eatAct = BodyConfig.EatAct(
                foods = mapOf(
                    BodyType.SILVER_COIN to BodyConfig.Food(
                        healthDiffPerSecond = -10f,
                        hunger = 0f,
                    ),
                    BodyType.GOLD_COIN to BodyConfig.Food(
                        healthDiffPerSecond = -10f,
                        hunger = 0f,
                    ),
                    BodyType.STAR to BodyConfig.Food(
                        healthDiffPerSecond = -10f,
                        hunger = 0f,
                    ),
                    BodyType.DIAMOND to BodyConfig.Food(
                        healthDiffPerSecond = -10f,
                        hunger = 0f,
                    ),
                ),
                drivingAccelerationX = 0.2f,
                drivingAccelerationY = 0.3f,
            ),
            swimActX = BodyConfig.SwimAct(
                drivingAcceleration = 0.1f,
                idlingTimeRandomStart = 2f,
                idlingTimeRandomEnd = 8f,
            ),
            swimActY = BodyConfig.SwimAct(
                drivingAcceleration = 0.15f,
                idlingTimeRandomStart = 2f,
                idlingTimeRandomEnd = 8f,
            ),
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "clyde",
                    duration = 1.125f,
                    mode = AnimationMode.LOOP,
                    interpolation = Interpolation.POW2_IN,
                ),
            ),
        ),
        BodyConfigInfo(
            type = BodyType.GASH,
            group = BodyGroup.PET,
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
            hunger = BodyConfig.Hunger(
                full = 10f,
                maxThreshold = 3f,
                diffPerSecond = -1f,
            ),
            eatAct = BodyConfig.EatAct(
                foods = mapOf(
                    BodyType.BEETLE to BodyConfig.Food(
                        healthDiffPerSecond = -10f,
                        hunger = 20f,
                    ),
                ),
                drivingAccelerationX = 0.8f,
                drivingAccelerationY = 0.5f,
            ),
            swimActX = BodyConfig.SwimAct(
                drivingAcceleration = 0.2f,
                idlingTimeRandomStart = 2f,
                idlingTimeRandomEnd = 8f,
            ),
            swimActY = BodyConfig.SwimAct(
                drivingAcceleration = 0.125f,
                idlingTimeRandomStart = 6f,
                idlingTimeRandomEnd = 10f,
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
        ),
        BodyConfigInfo(
            type = BodyType.PRESTO,
            group = BodyGroup.PET,
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
                drivingAccelerationX = 0.4f,
                drivingAccelerationY = 0.25f,
            ),
            swimActX = BodyConfig.SwimAct(
                drivingAcceleration = 0.2f,
                idlingTimeRandomStart = 2f,
                idlingTimeRandomEnd = 8f,
            ),
            swimActY = BodyConfig.SwimAct(
                drivingAcceleration = 0.125f,
                idlingTimeRandomStart = 6f,
                idlingTimeRandomEnd = 10f,
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
        ),
        BodyConfigInfo(
            type = BodyType.SILVER_COIN,
            group = BodyGroup.MONEY,
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
            isDead = true,
            health = BodyConfig.Health(
                full = 1f,
            ),
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "silver_coin",
                    duration = 0.6f,
                    mode = AnimationMode.LOOP,
                ),
            ),
        ),
        BodyConfigInfo(
            type = BodyType.GOLD_COIN,
            group = BodyGroup.MONEY,
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
            isDead = true,
            health = BodyConfig.Health(
                full = 1f,
            ),
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "gold_coin",
                    duration = 0.6f,
                    mode = AnimationMode.LOOP,
                ),
            ),
        ),
        BodyConfigInfo(
            type = BodyType.STAR,
            group = BodyGroup.MONEY,
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
            isDead = true,
            health = BodyConfig.Health(
                full = 1f,
            ),
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "star",
                    duration = 0.6f,
                    mode = AnimationMode.LOOP,
                ),
            ),
        ),
        BodyConfigInfo(
            type = BodyType.DIAMOND,
            group = BodyGroup.MONEY,
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
                index = 0,
                wh = BodyConfigInfo.WH.WIDTH,
            ),
            density = 1020f,
            isDead = true,
            health = BodyConfig.Health(
                full = 1f,
            ),
            animations = BodyConfig.Animations(
                swim = TextureRegionAnimation(
                    assetId = "diamond",
                    duration = 0.6f,
                    mode = AnimationMode.LOOP,
                ),
            ),
        ),
        BodyConfigInfo(
            type = BodyType.BEETLE,
            group = BodyGroup.MONEY,
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
            isDead = true,
            health = BodyConfig.Health(
                full = 10f,
            ),
            animations = BodyConfig.Animations(
                swim =  TextureRegionAnimation(
                    assetId = "beetle",
                    duration = 0.3f,
                    mode = AnimationMode.LOOP,
                ),
            ),
        ),
    )
}
