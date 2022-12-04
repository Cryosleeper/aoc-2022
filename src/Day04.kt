fun main() {

    fun List<String>.toRange(): IntRange {
        return first().toInt()..last().toInt()
    }

    fun List<String>.prepareData(): List<List<IntRange>> =
        map { line -> line.split(",").map { range -> range.split("-").toRange() } }

    fun IntRange.hasFullOverlap(anotherRange: IntRange): Boolean {
        if (this.first <= anotherRange.first && this.last >= anotherRange.last) {
            return true
        }
        if (this.first >= anotherRange.first && this.last <= anotherRange.last) {
            return true
        }
        return false
    }

    fun IntRange.hasPartialOverlap(anotherRange: IntRange): Boolean {
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

    fun IntRange.hasAnyOverlap(anotherRange: IntRange): Boolean {
        return hasFullOverlap(anotherRange) || hasPartialOverlap(anotherRange)
    }

    fun part1(input: List<String>): Int {
        return input
            .prepareData()
            .count {
                it.first().hasFullOverlap(it.last())
            }
    }

    fun part2(input: List<String>): Int {
        return input
            .prepareData()
            .count {
                it.first().hasAnyOverlap(it.last())
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
