fun main() {
    fun part1(input: List<String>): Int {
        var max = 0
        var current = 0
        for (value in input) {
            if (value.isEmpty()) {
                if (max < current) {
                    max = current
                }
                current = 0
            } else {
                current += value.toInt()
            }
        }
        if (max < current) {
            max = current
        }
        return max
    }

    fun part2(input: List<String>): Int {
        val sums = mutableListOf<Int>()
        var current = 0
        for (value in input) {
            if (value.isEmpty()) {
                sums.add(current)
                current = 0
            } else {
                current += value.toInt()
            }
        }
        sums.add(current)
        return sums.sortedDescending().subList(0, 3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 10)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
