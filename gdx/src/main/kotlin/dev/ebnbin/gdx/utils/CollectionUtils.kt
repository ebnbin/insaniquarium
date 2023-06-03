package dev.ebnbin.gdx.utils

/**
 * @return A pair of removed and added elements.
 */
fun <E> Set<E>.diff(other: Set<E>): Pair<Set<E>, Set<E>> {
    val removed = this.filterNotTo(mutableSetOf()) { other.contains(it) }
    val added = other.filterNotTo(mutableSetOf()) { this.contains(it) }
    return removed to added
}
