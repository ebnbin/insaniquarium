package dev.ebnbin.insaniquarium.tank

class TankData {
    val width: Float = WIDTH_DP.dpToMeter
    val height: Float = HEIGHT_DP.dpToMeter

    companion object {
        private const val WIDTH_DP = 960f
        private const val HEIGHT_DP = 600f
    }
}
