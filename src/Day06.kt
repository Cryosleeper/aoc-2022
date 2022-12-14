fun main() {
    fun getIndexOfGroup(input: String, groupSize: Int): Int {
        val buffer = HashSet<Char>()
        var result = -1
        run breaking@{
            (groupSize-1 until input.length).forEach { index ->
                buffer.clear()
                (index - groupSize + 1..index).forEach { buffer.add(input[it]) }
                if (buffer.size == groupSize) {
                    result = index + 1
                    return@breaking
                }
            }
        }
        return result
    }

    fun part1(input: List<String>): Int {
        return getIndexOfGroup(input[0], 4)
    }

    fun part2(input: List<String>): Int {
        return getIndexOfGroup(input[0], 14)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 19)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
