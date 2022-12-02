fun main() {
    fun part1(input: List<String>): Int {
        val outcomes = mapOf(
            "A X" to 3 + 1,
            "A Y" to 6 + 2,
            "A Z" to 0 + 3,
            "B X" to 0 + 1,
            "B Y" to 3 + 2,
            "B Z" to 6 + 3,
            "C X" to 6 + 1,
            "C Y" to 0 + 2,
            "C Z" to 3 + 3
        )
        var sum = 0
        for (line in input) {
            sum += outcomes.getValue(line)
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val outcomes = mapOf(
            "A X" to 0 + 3,
            "A Y" to 3 + 1,
            "A Z" to 6 + 2,
            "B X" to 0 + 1,
            "B Y" to 3 + 2,
            "B Z" to 6 + 3,
            "C X" to 0 + 2,
            "C Y" to 3 + 3,
            "C Z" to 6 + 1
        )
        var sum = 0
        for (line in input) {
            sum += outcomes.getValue(line)
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
