package dev.ebnbin.insaniquarium.body

import com.google.gson.annotations.Expose
import dev.ebnbin.gdx.animation.TextureRegionAnimation
import dev.ebnbin.gdx.asset.Asset
import dev.ebnbin.gdx.lifecycle.baseGame
import dev.ebnbin.gdx.utils.SerializableEnum

data class BodyAnimations(
    @Expose
    val swim: TextureRegionAnimation,
    @Expose
    val turn: TextureRegionAnimation? = null,
    @Expose
    val eat: TextureRegionAnimation? = null,
    @Expose
    val hungry: TextureRegionAnimation? = null,
    @Expose
    val hungryTurn: TextureRegionAnimation? = null,
    @Expose
    val hungryEat: TextureRegionAnimation? = null,
    @Expose
    val charged: TextureRegionAnimation? = null,
    @Expose
    val charge: TextureRegionAnimation? = null,
    @Expose
    val discharge: TextureRegionAnimation? = null,
    @Expose
    val hungryCharged: TextureRegionAnimation? = null,
    @Expose
    val hungryCharge: TextureRegionAnimation? = null,
    @Expose
    val hungryDischarge: TextureRegionAnimation? = null,
) {
    enum class Action(override val serializedName: String) : SerializableEnum {
        SWIM("swim"),
        TURN("turn"),
        EAT("eat"),
        CHARGE("charge"),
        DISCHARGE("discharge"),
        ;
    }

    fun get(action: Action, isHungry: Boolean, isCharged: Boolean): TextureRegionAnimation {
        return when (action) {
            Action.SWIM -> {
                when {
                    isHungry && !isCharged -> hungry ?: swim
                    !isHungry && isCharged -> charged ?: swim
                    isHungry && isCharged -> hungryCharged ?: swim
                    else -> swim
                }
            }
            Action.TURN -> {
                when {
                    isHungry && !isCharged -> hungryTurn ?: requireNotNull(turn)
                    else -> requireNotNull(turn)
                }
            }
            Action.EAT -> {
                when {
                    isHungry && !isCharged -> hungryEat ?: requireNotNull(eat)
                    else -> requireNotNull(eat)
                }
            }
            Action.CHARGE -> {
                requireNotNull(if (isHungry) hungryCharge else charge)
            }
            Action.DISCHARGE -> {
                requireNotNull(if (isHungry) hungryDischarge else discharge)
            }
        }
    }

    fun allAssets(): Set<Asset<*>> {
        return listOfNotNull(
            swim,
            turn,
            eat,
            hungry,
            hungryTurn,
            hungryEat,
            charged,
            charge,
            discharge,
            hungryCharged,
            hungryCharge,
            hungryDischarge,
        ).mapTo(mutableSetOf()) { baseGame.assets.texture.getValue(it.assetId) }
    }
}
