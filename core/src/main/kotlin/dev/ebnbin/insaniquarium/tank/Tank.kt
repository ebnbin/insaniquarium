package dev.ebnbin.insaniquarium.tank

class Tank(
    private val groupWrapper: TankGroupWrapper,
) {
    val data: TankData = TankData()

    init {
        groupWrapper.setSize(data.width, data.height)
    }
}
