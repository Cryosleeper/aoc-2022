fun main() {
    fun part1(input: List<String>): Int {
        val buffer = HashSet<Char>()
        var result = -1
        run breaking@{
            input[0].forEachIndexed { index, _ ->
                if (index >= 3) {
                    buffer.clear()
                    buffer.addAll(input[0].substring(index - 3, index + 1).toList())
                    if (buffer.size == 4) {
                        result = index + 1
                        return@breaking
                    }
                }
            }
        }
        return result
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 7)
    //check(part2(testInput) == 0)

    val input = readInput("Day06")
    println(part1(input))
    //println(part2(input))
}
