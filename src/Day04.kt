fun main() {

    fun List<String>.toRange(): IntRange {
        return first().toInt()..last().toInt()
    }

    fun IntRange.hasFullOverlap(second: IntRange): Boolean {
        if (this.first <= second.first && this.last >= second.last) {
            return true
        }
        if (this.first >= second.first && this.last <= second.last) {
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
        TODO()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    //check(part2(testInput) == 0)

    val input = readInput("Day04")
    println(part1(input))
    //println(part2(input))
}
