import java.util.Deque
import java.util.LinkedList

fun main() {
    fun part1(input: List<String>): String {
        val (stacks, moves) = input.parseInput()

        for (move in moves) {
            for (x in 0 until move.number) {
                stacks[move.to].addFirst(stacks[move.from].pop())
            }
        }

        return stacks.map { it.peek() }.joinToString("")
    }

    fun part2(input: List<String>): String {
        val (stacks, moves) = input.parseInput()

        val buffer = LinkedList<Char>()

        for (move in moves) {
            for (x in 0 until move.number) {
                buffer.addFirst(stacks[move.from].pop())
            }
            for (x in 0 until move.number) {
                stacks[move.to].addFirst(buffer.pop())
            }
        }

        return stacks.map { it.peek() }.joinToString("")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

data class Move (val from: Int, val to: Int, val number: Int)

fun List<String>.parseInput(): Pair<List<Deque<Char>>, List<Move>> {
    val inputDividerIndex = indexOf("")
    val initialStacks = subList(0, inputDividerIndex)
    val initialMoves = subList(inputDividerIndex + 1, size)

    val stacks = initialStacks.toQueues()
    val moves = initialMoves.toMoves()
    return Pair(stacks, moves)
}

private fun List<String>.toMoves(): List<Move> {
    val result = mutableListOf<Move>()
    val regex = "\\d+".toRegex()
    for (line in this) {
        regex.findAll(line).toList().apply {
            result.add(Move(this[1].value.toInt() - 1, this[2].value.toInt() - 1, this[0].value.toInt()))
        }
    }
    return result
}

private fun List<String>.toQueues(): List<Deque<Char>> {
    val input = reversed()
    val stackNumber = (input[0].length + 2) / 4
    val stacks = (0 until stackNumber).map { LinkedList<Char>() }
    val regex = "[A-Z]+".toRegex()
    for (line in input.subList(1, input.size)) {
        regex.findAll(line).forEach {
            stacks[it.range.first / 4].push(it.value[0])
        }
    }
    return stacks
}
