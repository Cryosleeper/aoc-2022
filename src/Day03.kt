fun main() {
    fun part1(input: List<String>): Int {
        val misplacedItems = mutableListOf<Char>()
        for (line in input) {
            val left = line.substring(0, line.length/2).toHashSet()
            val right = line.substring(line.length/2)

            for (c in right) {
                if (left.contains(c)) {
                    misplacedItems.add(c)
                    break
                }
            }
        }
        return misplacedItems.sumOf {
            if (it in 'a'..'z') {
                it - 'a' + 1
            } else {
                it - 'A' + 27
            }
        }
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    //check(part2(testInput) == 0)

    val input = readInput("Day03")
    println(part1(input))
    //println(part2(input))
}
