package dev.ebnbin.kgdx.util

private val START_MILLIS = System.currentTimeMillis()
private val START_NANO = System.nanoTime()

fun nanoTimestamp(): Long {
    return START_MILLIS * 1_000_000 + System.nanoTime() - START_NANO
}
