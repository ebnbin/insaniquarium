package dev.ebnbin.insaniquarium.body

interface BodyModifier {
    fun canTouch(data: BodyData): Boolean = true

    fun canDrop(data: BodyData): Boolean = true

    fun dropXOffset(data: BodyData): Float = 0f

    fun dropVelocityX(data: BodyData): Float = 0f

    companion object {
        private val NIKO: BodyModifier = object : BodyModifier {
            override fun canTouch(data: BodyData): Boolean {
                return data.isCharged &&
                    data.state.animationData.action != BodyAnimations.Action.CHARGE &&
                    data.state.hunger == data.body.config.hunger!!.full
            }
        }

        private val ZORF: BodyModifier = object : BodyModifier {
            override fun canDrop(data: BodyData): Boolean {
                return data.state.animationData.action == BodyAnimations.Action.DROP &&
                    data.state.animationData.stateTick == data.body.config.animations.drop!!.ticks - 1
            }

            override fun dropXOffset(data: BodyData): Float {
                val sign = if (data.state.animationData.isFacingRight) 1f else -1f
                return sign * 0.0775f
            }

            override fun dropVelocityX(data: BodyData): Float {
                val sign = if (data.state.animationData.isFacingRight) 1f else -1f
                return sign * 1f
            }
        }

        fun get(bodyType: BodyType): BodyModifier {
            return when (bodyType) {
                BodyType.NIKO -> NIKO
                BodyType.ZORF -> ZORF
                else -> object : BodyModifier {}
            }
        }
    }
}
