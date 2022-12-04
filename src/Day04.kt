fun main() {

    fun List<String>.toRange(): IntRange {
        return first().toInt()..last().toInt()
    }

    fun IntRange.hasFullOverlap(anotherRange: IntRange): Boolean {
        if (this.first <= anotherRange.first && this.last >= anotherRange.last) {
            return true
        }
        if (this.first >= anotherRange.first && this.last <= anotherRange.last) {
            return true
        }
        return false
    }

    fun IntRange.hasAnyOverlap(anotherRange: IntRange): Boolean {
        if (this.first <= anotherRange.first && this.last >= anotherRange.first) {
            return true
        }
        if (this.first <= anotherRange.last && this.last >= anotherRange.last) {
            return true
        }
        if (hasFullOverlap(anotherRange)) {
            return true
        }
        return false
    }

    fun part1(input: List<String>): Int {
        var count = 0
        for (line in input) {
            val (first, second) = line.split(",").map { it.split("-").toRange() }
            if (first.hasFullOverlap(second)) {
                count++
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        var count = 0
        for (line in input) {
            val (first, second) = line.split(",").map { it.split("-").toRange() }
            if (first.hasAnyOverlap(second)) {
                count++
            }
        }
        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
