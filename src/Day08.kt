import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        val numbers = input.toMatrix()
        return countTrees(numbers)
    }

    fun part2(input: List<String>): Int {
        val numbers = input.toMatrix()
        return countScenic(numbers)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

private fun List<String>.toMatrix(): List<List<Int>> = this.map { it.toList().map { it.digitToInt() } }

private fun countTrees(input: List<List<Int>>): Int {
    var result = 0

    input.forEachIndexed { lineIndex, line ->
        line.forEachIndexed { columnIndex, tree ->
            if (input[lineIndex].subList(0, columnIndex+1).filter { it >= tree }.size == 1) result++
            else if (input[lineIndex].subList(columnIndex, input[lineIndex].size).filter { it >= tree }.size == 1) result++
            else if (input.subList(0, lineIndex+1).map { it[columnIndex] }.filter { it >= tree }.size == 1) result++
            else if (input.subList(lineIndex, input.size).map { it[columnIndex] }.filter { it >= tree }.size == 1) result++
        }
    }

    return result
}

private fun countScenic(input: List<List<Int>>): Int {

    fun minNonZero(a: Int, b: Int): Int {
        return if (a == 0) b else if (b == 0) a else min(a, b)
    }

    var maxScenic = 0

    input.forEachIndexed { lineIndex, line ->
        line.forEachIndexed { columnIndex, tree ->
            val left = input[lineIndex].subList(0, columnIndex).asReversed()
            val leftCount = minNonZero(left.indexOfFirst { it >= tree } + 1, left.size)
            val right = input[lineIndex].subList(columnIndex+1, input[lineIndex].size)
            val rightCount = minNonZero(right.indexOfFirst { it >= tree } + 1, right.size)
            val top = input.subList(0, lineIndex).asReversed().map { it[columnIndex] }
            val topCount = minNonZero(top.indexOfFirst { it >= tree } + 1, top.size)
            val bottom = input.subList(lineIndex+1, input.size).map { it[columnIndex] }
            val bottomCount = minNonZero(bottom.indexOfFirst { it >= tree } + 1, bottom.size)

            maxScenic = max(maxScenic, leftCount * rightCount * topCount * bottomCount)
        }
    }

    return maxScenic
}
