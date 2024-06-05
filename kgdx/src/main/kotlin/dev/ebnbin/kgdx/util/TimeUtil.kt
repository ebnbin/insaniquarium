package dev.ebnbin.kgdx.util

private val START_MILLIS = System.currentTimeMillis()
private val START_NANO = System.nanoTime()

fun nanoTimestamp(): Long {
    return START_MILLIS * 1_000_000 + System.nanoTime() - START_NANO
}

data class Time(
    val hour: Int,
    val minute: Int,
    val second: Int,
    val millisecond: Int,
)

fun Float.toTime(): Time {
    return Time(
        hour = (this / 3600).toInt(),
        minute = (this % 3600 / 60).toInt(),
        second = (this % 60).toInt(),
        millisecond = (this % 1 * 1000).toInt(),
    )
}
