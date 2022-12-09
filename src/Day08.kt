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

    fun viewDistance(lowerTrees: Int, distanceToEdge: Int) = if (lowerTrees == 0) distanceToEdge else min(lowerTrees, distanceToEdge)

    fun List<Int>.distanceToFirstAsHigh(height: Int) = indexOfFirst { it >= height } + 1

    var maxScenic = 0

    input.forEachIndexed { lineIndex, line ->
        line.forEachIndexed { columnIndex, tree ->
            val toLeft = input[lineIndex].subList(0, columnIndex).asReversed()
            val leftViewDistance = viewDistance(toLeft.distanceToFirstAsHigh(tree), toLeft.size)

            val toRight = input[lineIndex].subList(columnIndex+1, input[lineIndex].size)
            val rightViewDistance = viewDistance(toRight.distanceToFirstAsHigh(tree), toRight.size)

            val toTop = input.subList(0, lineIndex).asReversed().map { it[columnIndex] }
            val topViewDistance = viewDistance(toTop.distanceToFirstAsHigh(tree), toTop.size)

            val toBottom = input.subList(lineIndex+1, input.size).map { it[columnIndex] }
            val bottomViewDistance = viewDistance(toBottom.distanceToFirstAsHigh(tree), toBottom.size)

            maxScenic = max(maxScenic, leftViewDistance * rightViewDistance * topViewDistance * bottomViewDistance)
        }
    }

    return maxScenic
}
