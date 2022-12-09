fun main() {
    fun part1(input: List<String>): Int {
        val numbers = input.toMatrix()
        return countTrees(numbers)
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    //check(part2(testInput) == 0)

    val input = readInput("Day08")
    println(part1(input))
    //println(part2(input))
}

private fun List<String>.toMatrix(): List<List<Int>> = this.map { it.toList().map { it.digitToInt() } }

private fun countTrees(input: List<List<Int>>): Int {
    val visible = input.map { it.map { false }.toMutableList() }.toMutableList()

    input.forEachIndexed { lineIndex, line ->
        line.forEachIndexed { columnIndex, tree ->
            if (input[lineIndex].subList(0, columnIndex+1).filter { it >= tree }.size == 1) visible[lineIndex][columnIndex] = true
            else if (input[lineIndex].subList(columnIndex, input[lineIndex].size).filter { it >= tree }.size == 1) visible[lineIndex][columnIndex] = true
            else if (input.subList(0, lineIndex+1).map { it[columnIndex] }.filter { it >= tree }.size == 1) visible[lineIndex][columnIndex] = true
            else if (input.subList(lineIndex, input.size).map { it[columnIndex] }.filter { it >= tree }.size == 1) visible[lineIndex][columnIndex] = true
        }
    }

    return visible.sumOf { it.count { it } }
}