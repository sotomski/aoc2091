package day04

class BruteforceCracker(private val lower: Int, private val upper: Int) {

    fun passwordCount() = (lower..upper).filter { isPassword(it) }.distinct().count()

    fun passwordCountWithStrictPairs() = (lower..upper).filter { isPasswordWithStrictPairs(it) }.distinct().count()

    fun isPasswordWithStrictPairs(candidate: Int): Boolean {
        val digits = candidate.toString().toCharArray().map { it.toInt() }

        // Must be 6 digits
        if (digits.count() != 6) {
            return false
        }

        // Must have at least one pair that's not part of a bigger group
        val pairCounts = mutableListOf(1)
        var pairIndex = 0

        digits.windowed(2, 1).forEach {
            val prev = it[0]
            val next = it[1]

            if (prev == next) {
                pairCounts[pairIndex] = pairCounts[pairIndex] + 1
            } else {
                pairCounts.add(1)
                pairIndex++
            }
        }

        if (!pairCounts.contains(2)) {
            return false
        }

        // Must be monotonic
        digits.windowed(2, 1).forEach {
            if (it[0] > it[1]) {
                return false
            }
        }

        return true
    }

    fun isPassword(candidate: Int): Boolean {
        val digits = candidate.toString().toCharArray().map { it.toInt() }

        // Must be 6 digits
        if (digits.count() != 6) {
            return false
        }

        // Must have at least 2 identical neighbours
        val sameNeighbourCount = digits.windowed(2, 1).count { it[0] == it[1] }
        if (sameNeighbourCount < 1) {
            return false
        }

        // Must be monotonic
        digits.windowed(2, 1).forEach {
            if (it[0] > it[1]) {
                return false
            }
        }

        return true
    }
}