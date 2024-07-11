package dev.ebnbin.insaniquarium.tank

class TankGroupWrapper(
    private val tankGroup: TankGroup,
) {
    fun setSize(width: Float, height: Float) {
        tankGroup.setSize(width, height)
    }
}
