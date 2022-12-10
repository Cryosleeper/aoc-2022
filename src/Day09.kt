import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val steps = input.toSteps()
        val visitedPlaces = mutableSetOf<Position>()
        val head = Position(10000, 10000)
        val tail = Position(10000, 10000)
        steps.forEach { step ->
            when (step) {
                Step.UP -> head.y--
                Step.DOWN -> head.y++
                Step.LEFT -> head.x--
                Step.RIGHT -> head.x++
            }
            if (tail.rangeTo(head) > 2) {
                if (tail.x > head.x && tail.y > head.y) {
                    tail.x--
                    tail.y--
                } else if (tail.x > head.x && tail.y < head.y) {
                    tail.x--
                    tail.y++
                } else if (tail.x < head.x && tail.y > head.y) {
                    tail.x++
                    tail.y--
                } else if (tail.x < head.x && tail.y < head.y) {
                    tail.x++
                    tail.y++
                }
            } else if (tail.x - head.x == 2) {
                tail.x--
            } else if (tail.x - head.x == -2) {
                tail.x++
            } else if (tail.y - head.y == 2) {
                tail.y--
            } else if (tail.y - head.y == -2) {
                tail.y++
            }
            visitedPlaces.add(tail.copy())
        }
        return visitedPlaces.size
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    //check(part2(testInput) == 0)

    val input = readInput("Day09")
    println(part1(input))
    //println(part2(input))
}

private fun List<String>.toSteps(): List<Step> {
    val result = mutableListOf<Step>()
    this.forEach { line ->
        val (direction, distance) = line.split(" ")
        repeat(distance.toInt()) { result.add(Step.fromLetter(direction)) }
    }
    return result
}

private enum class Step {
    UP, DOWN, LEFT, RIGHT;

    companion object {
        fun fromLetter(letter: String): Step {
            return when (letter) {
                "R" -> RIGHT
                "L" -> LEFT
                "U" -> UP
                "D" -> DOWN
                else -> throw IllegalArgumentException("Unknown step: $letter")
            }
        }
    }
}

private data class Position(var x: Int, var y: Int) {
    fun rangeTo(position: Position): Int {
        return abs(x - position.x) + abs(y - position.y)
    }
}