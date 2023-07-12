import dev.ebnbin.gdx.animation.TextureRegionAnimation
import dev.ebnbin.gdx.animation.AnimationMode
import dev.ebnbin.gdx.asset.Assets
import dev.ebnbin.gdx.utils.Interpolation
import dev.ebnbin.gdx.utils.fromJson
import dev.ebnbin.gdx.utils.unitToMeter
import dev.ebnbin.insaniquarium.body.BodyAnimations
import dev.ebnbin.insaniquarium.body.BodyConfig
import dev.ebnbin.insaniquarium.body.BodyGroup
import dev.ebnbin.insaniquarium.body.BodyType
import java.awt.image.BufferedImage
import java.io.File

object ConfigInfo {
    private enum class WH {
        WIDTH,
        HEIGHT,
        ;
    }
    
    private val assets: Assets = File("../assets/assets.json").readText().fromJson()

    private val imageCache: MutableMap<String, BufferedImage> = mutableMapOf()
    
    private fun size(
        textureName: String,
        index: Int? = null,
        wh: WH,
    ): Float {
        val image = imageCache.getOrPut(textureName) {
            File(TextureInfo.dstDir, "${textureName}.png").readImage()
        }
        val region = assets.texture.getValue(textureName).region
        require(region != null)
        val split = image.split(region.row, region.column)
        val nonTransparentSize = split[index ?: region.startIndex].nonTransparentSize()
        return if (wh == WH.WIDTH) {
            nonTransparentSize.first.toFloat().unitToMeter
        } else {
            nonTransparentSize.second.toFloat().unitToMeter
        }
    }
    
    val bodyMap: Map<BodyType, BodyConfig> = mapOf(
        BodyType.FISH_FOOD to BodyConfig(
            group = BodyGroup.FOOD,
            width = size(
                textureName = "fish_food",
                index = 0,
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "fish_food",
                index = 0,
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "fish_food",
                index = 0,
                wh = WH.WIDTH,
            ),
            density = 1020f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "fish_food",
                    ticks = 20,
                    mode = AnimationMode.LOOP,
                ),
            ),
            isDead = true,
            health = BodyConfig.Health(
                full = 2,
            ),
        ),
        BodyType.STAR_POTION to BodyConfig(
            group = BodyGroup.FOOD,
            width = size(
                textureName = "star_potion",
                index = 0,
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "star_potion",
                index = 0,
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "star_potion",
                index = 0,
                wh = WH.WIDTH,
            ),
            density = 1020f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "star_potion",
                    ticks = 20,
                    mode = AnimationMode.LOOP,
                ),
            ),
            isDead = true,
            health = BodyConfig.Health(
                full = 2,
            ),
        ),
        BodyType.GUPPY_SMALL to BodyConfig(
            group = BodyGroup.FISH,
            width = size(
                textureName = "guppy_small",
                index = 0,
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "guppy_small",
                index = 0,
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "guppy_small_turn",
                index = 5,
                wh = WH.WIDTH,
            ),
            drivingAccelerationX = 0.4f,
            drivingAccelerationY = 0.25f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "guppy_small",
                    ticks = 10,
                    mode = AnimationMode.LOOP,
                    finishTicks = 0,
                ),
                turn = TextureRegionAnimation(
                    assetId = "guppy_small_turn",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
                eat = TextureRegionAnimation(
                    assetId = "guppy_small_eat",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
                hungry = TextureRegionAnimation(
                    assetId = "guppy_small_hungry",
                    ticks = 10,
                    mode = AnimationMode.LOOP,
                    finishTicks = 0,
                ),
                hungryTurn = TextureRegionAnimation(
                    assetId = "guppy_small_hungry_turn",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
                hungryEat = TextureRegionAnimation(
                    assetId = "guppy_small_hungry_eat",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
            ),
            health = BodyConfig.Health(
                full = 40,
            ),
            hunger = BodyConfig.Hunger(
                full = 2000,
                max = 3000,
                hungry = 1000,
                diffPerTick = -5,
                transformation = BodyType.GUPPY_SMALL_CORPSE,
            ),
            growth = BodyConfig.Growth(
                full = 10,
                transformation = BodyType.GUPPY_MEDIUM,
            ),
            eatAct = BodyConfig.EatAct(
                foods = mapOf(
                    BodyType.FISH_FOOD to BodyConfig.Food(
                        healthDiffPerTick = -1,
                        hunger = 2000,
                        growth = 5,
                    ),
                ),
            ),
            swimActX = BodyConfig.SwimAct(
                idlingTicksMin = 40,
                idlingTicksMax = 160,
            ),
            swimActY = BodyConfig.SwimAct(
                idlingTicksMin = 120,
                idlingTicksMax = 200,
            ),
        ),
        BodyType.GUPPY_MEDIUM to BodyConfig(
            group = BodyGroup.FISH,
            width = size(
                textureName = "guppy_medium",
                index = 0,
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "guppy_medium",
                index = 0,
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "guppy_medium_turn",
                index = 5,
                wh = WH.WIDTH,
            ),
            drivingAccelerationX = 0.4f,
            drivingAccelerationY = 0.25f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "guppy_medium",
                    ticks = 10,
                    mode = AnimationMode.LOOP,
                    finishTicks = 0,
                ),
                turn = TextureRegionAnimation(
                    assetId = "guppy_medium_turn",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
                eat = TextureRegionAnimation(
                    assetId = "guppy_medium_eat",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
                hungry = TextureRegionAnimation(
                    assetId = "guppy_medium_hungry",
                    ticks = 10,
                    mode = AnimationMode.LOOP,
                    finishTicks = 0,
                ),
                hungryTurn = TextureRegionAnimation(
                    assetId = "guppy_medium_hungry_turn",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
                hungryEat = TextureRegionAnimation(
                    assetId = "guppy_medium_hungry_eat",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
            ),
            health = BodyConfig.Health(
                full = 40,
            ),
            hunger = BodyConfig.Hunger(
                full = 2000,
                max = 3000,
                hungry = 1000,
                diffPerTick = -5,
                transformation = BodyType.GUPPY_MEDIUM_CORPSE,
            ),
            growth = BodyConfig.Growth(
                full = 12,
                transformation = BodyType.GUPPY_LARGE,
            ),
            drop = BodyConfig.Drop(
                full = 160,
                diffPerTick = 1,
                production = BodyType.SILVER_COIN,
            ),
            eatAct = BodyConfig.EatAct(
                foods = mapOf(
                    BodyType.FISH_FOOD to BodyConfig.Food(
                        healthDiffPerTick = -1,
                        hunger = 2000,
                        growth = 5,
                    ),
                ),
            ),
            swimActX = BodyConfig.SwimAct(
                idlingTicksMin = 40,
                idlingTicksMax = 160,
            ),
            swimActY = BodyConfig.SwimAct(
                idlingTicksMin = 120,
                idlingTicksMax = 200,
            ),
        ),
        BodyType.GUPPY_LARGE to BodyConfig(
            group = BodyGroup.FISH,
            width = size(
                textureName = "guppy_large",
                index = 0,
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "guppy_large",
                index = 0,
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "guppy_large_turn",
                index = 5,
                wh = WH.WIDTH,
            ),
            drivingAccelerationX = 0.4f,
            drivingAccelerationY = 0.25f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "guppy_large",
                    ticks = 10,
                    mode = AnimationMode.LOOP,
                    finishTicks = 0,
                ),
                turn = TextureRegionAnimation(
                    assetId = "guppy_large_turn",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
                eat = TextureRegionAnimation(
                    assetId = "guppy_large_eat",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
                hungry = TextureRegionAnimation(
                    assetId = "guppy_large_hungry",
                    ticks = 10,
                    mode = AnimationMode.LOOP,
                    finishTicks = 0,
                ),
                hungryTurn = TextureRegionAnimation(
                    assetId = "guppy_large_hungry_turn",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
                hungryEat = TextureRegionAnimation(
                    assetId = "guppy_large_hungry_eat",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
            ),
            health = BodyConfig.Health(
                full = 40,
            ),
            hunger = BodyConfig.Hunger(
                full = 2000,
                max = 3000,
                hungry = 1000,
                diffPerTick = -5,
                transformation = BodyType.GUPPY_LARGE_CORPSE,
            ),
            growth = BodyConfig.Growth(
                full = 18,
                transformation = BodyType.GUPPY_KING,
            ),
            drop = BodyConfig.Drop(
                full = 160,
                diffPerTick = 1,
                production = BodyType.GOLD_COIN,
            ),
            eatAct = BodyConfig.EatAct(
                foods = mapOf(
                    BodyType.FISH_FOOD to BodyConfig.Food(
                        healthDiffPerTick = -1,
                        hunger = 2000,
                        growth = 5,
                    ),
                ),
            ),
            swimActX = BodyConfig.SwimAct(
                idlingTicksMin = 40,
                idlingTicksMax = 160,
            ),
            swimActY = BodyConfig.SwimAct(
                idlingTicksMin = 120,
                idlingTicksMax = 200,
            ),
        ),
        BodyType.GUPPY_KING to BodyConfig(
            group = BodyGroup.FISH,
            width = size(
                textureName = "guppy_king",
                index = 0,
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "guppy_king",
                index = 0,
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "guppy_king_turn",
                index = 5,
                wh = WH.WIDTH,
            ),
            drivingAccelerationX = 0.4f,
            drivingAccelerationY = 0.25f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "guppy_king",
                    ticks = 10,
                    mode = AnimationMode.LOOP,
                    finishTicks = 0,
                ),
                turn = TextureRegionAnimation(
                    assetId = "guppy_king_turn",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
                eat = TextureRegionAnimation(
                    assetId = "guppy_king_eat",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
                hungry = TextureRegionAnimation(
                    assetId = "guppy_king_hungry",
                    ticks = 10,
                    mode = AnimationMode.LOOP,
                    finishTicks = 0,
                ),
                hungryTurn = TextureRegionAnimation(
                    assetId = "guppy_king_hungry_turn",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
                hungryEat = TextureRegionAnimation(
                    assetId = "guppy_king_hungry_eat",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
            ),
            health = BodyConfig.Health(
                full = 40,
            ),
            hunger = BodyConfig.Hunger(
                full = 2000,
                max = 3000,
                hungry = 1000,
                diffPerTick = -5,
                transformation = BodyType.GUPPY_KING_CORPSE,
            ),
            drop = BodyConfig.Drop(
                full = 160,
                diffPerTick = 1,
                production = BodyType.DIAMOND,
            ),
            eatAct = BodyConfig.EatAct(
                foods = mapOf(
                    BodyType.FISH_FOOD to BodyConfig.Food(
                        healthDiffPerTick = -1,
                        hunger = 2000,
                    ),
                ),
            ),
            swimActX = BodyConfig.SwimAct(
                idlingTicksMin = 40,
                idlingTicksMax = 160,
            ),
            swimActY = BodyConfig.SwimAct(
                idlingTicksMin = 120,
                idlingTicksMax = 200,
            ),
        ),
        BodyType.STARCATCHER to BodyConfig(
            group = BodyGroup.FISH,
            width = size(
                textureName = "starcatcher",
                index = 0,
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "starcatcher",
                index = 0,
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "starcatcher",
                index = 0,
                wh = WH.WIDTH,
            ),
            density = 1080f,
            drivingAccelerationX = 0.3f,
            drivingAccelerationY = 0f,
            bottomFrictionCoefficient = 0.01f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "starcatcher",
                    ticks = 20,
                    mode = AnimationMode.LOOP,
                    finishTicks = 0,
                ),
                hungry = TextureRegionAnimation(
                    assetId = "starcatcher_hungry",
                    ticks = 20,
                    mode = AnimationMode.LOOP,
                    finishTicks = 0,
                ),
            ),
            hunger = BodyConfig.Hunger(
                full = 2000,
                max = 3000,
                hungry = 1000,
                diffPerTick = -5,
                transformation = BodyType.STARCATCHER_CORPSE,
            ),
            drop = BodyConfig.Drop(
                full = 160,
                production = BodyType.BEETLE,
            ),
            eatAct = BodyConfig.EatAct(
                foods = mapOf(
                    BodyType.STAR to BodyConfig.Food(
                        healthDiffPerTick = -1,
                        hunger = 2000,
                        drop = 160,
                    ),
                ),
            ),
            swimActX = BodyConfig.SwimAct(
                idlingTicksMin = 40,
                idlingTicksMax = 160,
            ),
        ),
        BodyType.BEETLEMUNCHER to BodyConfig(
            group = BodyGroup.FISH,
            width = size(
                textureName = "beetlemuncher",
                index = 0,
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "beetlemuncher",
                index = 0,
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "beetlemuncher_turn",
                index = 5,
                wh = WH.WIDTH,
            ),
            drivingAccelerationX = 0.4f,
            drivingAccelerationY = 0.25f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "beetlemuncher",
                    ticks = 10,
                    mode = AnimationMode.LOOP,
                    finishTicks = 0,
                ),
                turn = TextureRegionAnimation(
                    assetId = "beetlemuncher_turn",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
                eat = TextureRegionAnimation(
                    assetId = "beetlemuncher_eat",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
                hungry = TextureRegionAnimation(
                    assetId = "beetlemuncher_hungry",
                    ticks = 10,
                    mode = AnimationMode.LOOP,
                    finishTicks = 0,
                ),
                hungryTurn = TextureRegionAnimation(
                    assetId = "beetlemuncher_hungry_turn",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
                hungryEat = TextureRegionAnimation(
                    assetId = "beetlemuncher_hungry_eat",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
            ),
            hunger = BodyConfig.Hunger(
                full = 2000,
                max = 3000,
                hungry = 1000,
                diffPerTick = -5,
                transformation = BodyType.BEETLEMUNCHER_CORPSE,
            ),
            eatAct = BodyConfig.EatAct(
                foods = mapOf(
                    BodyType.STAR_POTION to BodyConfig.Food(
                        healthDiffPerTick = -1,
                        hunger = 2000,
                    ),
                ),
            ),
            swimActX = BodyConfig.SwimAct(
                idlingTicksMin = 40,
                idlingTicksMax = 160,
            ),
            swimActY = BodyConfig.SwimAct(
                idlingTicksMin = 120,
                idlingTicksMax = 200,
            ),
        ),
        BodyType.GUPPY_SMALL_CORPSE to BodyConfig(
            group = BodyGroup.FISH,
            width = size(
                textureName = "guppy_small",
                index = 0,
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "guppy_small",
                index = 0,
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "guppy_small_turn",
                index = 5,
                wh = WH.WIDTH,
            ),
            density = 1020f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "guppy_small_die",
                    ticks = 20,
                    mode = AnimationMode.NORMAL,
                ),
            ),
            isDead = true,
        ),
        BodyType.GUPPY_MEDIUM_CORPSE to BodyConfig(
            group = BodyGroup.FISH,
            width = size(
                textureName = "guppy_medium",
                index = 0,
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "guppy_medium",
                index = 0,
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "guppy_medium_turn",
                index = 5,
                wh = WH.WIDTH,
            ),
            density = 1020f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "guppy_medium_die",
                    ticks = 20,
                    mode = AnimationMode.NORMAL,
                ),
            ),
            isDead = true,
        ),
        BodyType.GUPPY_LARGE_CORPSE to BodyConfig(
            group = BodyGroup.FISH,
            width = size(
                textureName = "guppy_large",
                index = 0,
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "guppy_large",
                index = 0,
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "guppy_large_turn",
                index = 5,
                wh = WH.WIDTH,
            ),
            density = 1020f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "guppy_large_die",
                    ticks = 20,
                    mode = AnimationMode.NORMAL,
                ),
            ),
            isDead = true,
        ),
        BodyType.GUPPY_KING_CORPSE to BodyConfig(
            group = BodyGroup.FISH,
            width = size(
                textureName = "guppy_king",
                index = 0,
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "guppy_king",
                index = 0,
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "guppy_king_turn",
                index = 5,
                wh = WH.WIDTH,
            ),
            density = 1020f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "guppy_king_die",
                    ticks = 20,
                    mode = AnimationMode.NORMAL,
                ),
            ),
            isDead = true,
        ),
        BodyType.STARCATCHER_CORPSE to BodyConfig(
            group = BodyGroup.FISH,
            width = size(
                textureName = "starcatcher",
                index = 0,
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "starcatcher",
                index = 0,
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "starcatcher",
                index = 0,
                wh = WH.WIDTH,
            ),
            density = 1020f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "starcatcher_die",
                    ticks = 20,
                    mode = AnimationMode.NORMAL,
                ),
            ),
            isDead = true,
        ),
        BodyType.BEETLEMUNCHER_CORPSE to BodyConfig(
            group = BodyGroup.FISH,
            width = size(
                textureName = "beetlemuncher",
                index = 0,
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "beetlemuncher",
                index = 0,
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "beetlemuncher_turn",
                index = 5,
                wh = WH.WIDTH,
            ),
            density = 1020f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "beetlemuncher_die",
                    ticks = 20,
                    mode = AnimationMode.NORMAL,
                ),
            ),
            isDead = true,
        ),
        BodyType.SYLVESTER to BodyConfig(
            group = BodyGroup.ALIEN,
            width = size(
                textureName = "sylvester",
                index = 0,
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "sylvester",
                index = 0,
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "sylvester_turn",
                index = 5,
                wh = WH.WIDTH,
            ),
            drivingAccelerationX = 0.4f,
            drivingAccelerationY = 0.25f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "sylvester",
                    ticks = 10,
                    mode = AnimationMode.LOOP,
                    finishTicks = 0,
                ),
                turn = TextureRegionAnimation(
                    assetId = "sylvester_turn",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
            ),
            health = BodyConfig.Health(
                full = 800,
            ),
            hunger = BodyConfig.Hunger(
                full = 2000,
            ),
            eatAct = BodyConfig.EatAct(
                foods = mapOf(
                    BodyType.GUPPY_SMALL to BodyConfig.Food(
                        healthDiffPerTick = -10,
                    ),
                    BodyType.GUPPY_MEDIUM to BodyConfig.Food(
                        healthDiffPerTick = -10,
                    ),
                    BodyType.GUPPY_LARGE to BodyConfig.Food(
                        healthDiffPerTick = -10,
                    ),
                    BodyType.GUPPY_KING to BodyConfig.Food(
                        healthDiffPerTick = -10,
                    ),
                ),
            ),
            swimActX = BodyConfig.SwimAct(
                idlingTicksMin = 0,
                idlingTicksMax = 0,
            ),
            swimActY = BodyConfig.SwimAct(
                idlingTicksMin = 0,
                idlingTicksMax = 0,
            ),
        ),
        BodyType.NIKO to BodyConfig(
            group = BodyGroup.PET,
            width = size(
                textureName = "niko",
                index = 0,
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "niko",
                index = 0,
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "niko",
                index = 0,
                wh = WH.WIDTH,
            ),
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "niko",
                    ticks = 10,
                    mode = AnimationMode.LOOP_PINGPONG,
                    finishTicks = 20,
                ),
                charged = TextureRegionAnimation(
                    assetId = "niko_opened",
                    ticks = 1,
                    mode = AnimationMode.LOOP,
                ),
                charge = TextureRegionAnimation(
                    assetId = "niko_open",
                    ticks = 9,
                    mode = AnimationMode.NORMAL,
                ),
                discharge = TextureRegionAnimation(
                    assetId = "niko_open",
                    ticks = 9,
                    mode = AnimationMode.REVERSED,
                ),
            ),
            energy = BodyConfig.Energy(
                full = 800,
                diffPerTick = 1,
                dischargeDiffPerTick = -8,
            ),
        ),
        BodyType.CLYDE to BodyConfig(
            group = BodyGroup.PET,
            width = size(
                textureName = "clyde",
                index = 0,
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "clyde",
                index = 0,
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "clyde",
                index = 0,
                wh = WH.WIDTH,
            ),
            drivingAccelerationX = 0.2f,
            drivingAccelerationY = 0.3f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "clyde",
                    ticks = 25,
                    mode = AnimationMode.LOOP,
                    interpolation = Interpolation.POW2_IN,
                ),
            ),
            eatAct = BodyConfig.EatAct(
                foods = mapOf(
                    BodyType.SILVER_COIN to BodyConfig.Food(
                        healthDiffPerTick = -1,
                    ),
                    BodyType.GOLD_COIN to BodyConfig.Food(
                        healthDiffPerTick = -1,
                    ),
                    BodyType.STAR to BodyConfig.Food(
                        healthDiffPerTick = -1,
                    ),
                    BodyType.DIAMOND to BodyConfig.Food(
                        healthDiffPerTick = -1,
                    ),
                ),
            ),
            swimActX = BodyConfig.SwimAct(
                idlingTicksMin = 40,
                idlingTicksMax = 160,
            ),
            swimActY = BodyConfig.SwimAct(
                idlingTicksMin = 40,
                idlingTicksMax = 160,
            ),
        ),
        BodyType.GASH to BodyConfig(
            group = BodyGroup.PET,
            width = size(
                textureName = "gash",
                index = 0,
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "gash",
                index = 0,
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "gash_turn",
                index = 5,
                wh = WH.WIDTH,
            ),
            drivingAccelerationX = 0.8f,
            drivingAccelerationY = 0.5f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "gash",
                    ticks = 10,
                    mode = AnimationMode.LOOP,
                    finishTicks = 0,
                ),
                turn = TextureRegionAnimation(
                    assetId = "gash_turn",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
                eat = TextureRegionAnimation(
                    assetId = "gash_eat",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
            ),
            hunger = BodyConfig.Hunger(
                full = 2000,
            ),
            eatAct = BodyConfig.EatAct(
                foods = mapOf(
                    BodyType.SYLVESTER to BodyConfig.Food(
                        healthDiffPerTick = -5,
                    ),
                ),
            ),
            swimActX = BodyConfig.SwimAct(
                drivingAccelerationMultiplier = 0.25f,
                idlingTicksMin = 40,
                idlingTicksMax = 160,
            ),
            swimActY = BodyConfig.SwimAct(
                drivingAccelerationMultiplier = 0.25f,
                idlingTicksMin = 120,
                idlingTicksMax = 200,
            ),
        ),
        BodyType.PRESTO to BodyConfig(
            group = BodyGroup.PET,
            width = size(
                textureName = "presto",
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "presto",
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "presto_turn",
                index = 5,
                wh = WH.WIDTH,
            ),
            drivingAccelerationX = 0.4f,
            drivingAccelerationY = 0.25f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "presto",
                    ticks = 10,
                    mode = AnimationMode.LOOP,
                    finishTicks = 0,
                ),
                turn = TextureRegionAnimation(
                    assetId = "presto_turn",
                    ticks = 10,
                    mode = AnimationMode.NORMAL,
                ),
            ),
            touchAct = BodyConfig.TouchAct(),
            swimActX = BodyConfig.SwimAct(
                idlingTicksMin = 40,
                idlingTicksMax = 160,
            ),
            swimActY = BodyConfig.SwimAct(
                idlingTicksMin = 120,
                idlingTicksMax = 200,
            ),
        ),
        BodyType.SILVER_COIN to BodyConfig(
            group = BodyGroup.MONEY,
            width = size(
                textureName = "silver_coin",
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "silver_coin",
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "silver_coin",
                index = 5,
                wh = WH.HEIGHT,
            ),
            density = 1020f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "silver_coin",
                    ticks = 12,
                    mode = AnimationMode.LOOP,
                ),
            ),
            isDead = true,
            health = BodyConfig.Health(
                full = 2,
            ),
        ),
        BodyType.GOLD_COIN to BodyConfig(
            group = BodyGroup.MONEY,
            width = size(
                textureName = "gold_coin",
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "gold_coin",
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "gold_coin",
                index = 5,
                wh = WH.HEIGHT,
            ),
            density = 1020f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "gold_coin",
                    ticks = 12,
                    mode = AnimationMode.LOOP,
                ),
            ),
            isDead = true,
            health = BodyConfig.Health(
                full = 2,
            ),
        ),
        BodyType.STAR to BodyConfig(
            group = BodyGroup.MONEY,
            width = size(
                textureName = "star",
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "star",
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "star",
                index = 5,
                wh = WH.WIDTH,
            ),
            density = 1010f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "star",
                    ticks = 12,
                    mode = AnimationMode.LOOP,
                ),
            ),
            isDead = true,
            health = BodyConfig.Health(
                full = 2,
            ),
        ),
        BodyType.DIAMOND to BodyConfig(
            group = BodyGroup.MONEY,
            width = size(
                textureName = "diamond",
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "diamond",
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "diamond",
                index = 0,
                wh = WH.WIDTH,
            ),
            density = 1020f,
            animations = BodyAnimations(
                swim = TextureRegionAnimation(
                    assetId = "diamond",
                    ticks = 12,
                    mode = AnimationMode.LOOP,
                ),
            ),
            isDead = true,
            health = BodyConfig.Health(
                full = 2,
            ),
        ),
        BodyType.BEETLE to BodyConfig(
            group = BodyGroup.MONEY,
            width = size(
                textureName = "beetle",
                wh = WH.WIDTH,
            ),
            height = size(
                textureName = "beetle",
                wh = WH.HEIGHT,
            ),
            depth = size(
                textureName = "beetle",
                index = 5,
                wh = WH.WIDTH,
            ),
            density = 990f,
            animations = BodyAnimations(
                swim =  TextureRegionAnimation(
                    assetId = "beetle",
                    ticks = 10,
                    mode = AnimationMode.LOOP,
                ),
            ),
            isDead = true,
            health = BodyConfig.Health(
                full = 2,
            ),
        ),
    ).also {
        imageCache.clear()
    }
}
