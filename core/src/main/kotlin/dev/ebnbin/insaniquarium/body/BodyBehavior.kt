package dev.ebnbin.insaniquarium.body

interface BodyBehavior {
    fun canTouch(data: BodyData): Boolean = true

    companion object {
        private val NIKO: BodyBehavior = object : BodyBehavior {
            override fun canTouch(data: BodyData): Boolean {
                return data.isCharged &&
                    data.state.animationData.action != BodyAnimations.Action.CHARGE &&
                    data.state.hunger == data.body.config.hunger!!.full
            }
        }

        fun get(bodyType: BodyType): BodyBehavior {
            return when (bodyType) {
                BodyType.NIKO -> NIKO
                else -> object : BodyBehavior {}
            }
        }
    }
}
