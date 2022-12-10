import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val steps = input.toSteps()
        val visitedPlaces = mutableSetOf<Position>()
        val head = Position(0, 0)
        val tail = Position(0, 0)
        steps.forEach { step ->
            when (step) {
                Step.UP -> head.y--
                Step.DOWN -> head.y++
                Step.LEFT -> head.x--
                Step.RIGHT -> head.x++
            }
            tail.moveTowards(head)
            visitedPlaces.add(tail.copy())
        }
        return visitedPlaces.size
    }

    fun part2(input: List<String>): Int {
        val steps = input.toSteps()
        val visitedPlaces = mutableSetOf<Position>()
        val knots = (0 until 10).map { Position(10000, 10000) }
        steps.forEach { step ->
            when (step) {
                Step.UP -> knots.first().y--
                Step.DOWN -> knots.first().y++
                Step.LEFT -> knots.first().x--
                Step.RIGHT -> knots.first().x++
            }
            knots.forEachIndexed { index, position ->
                if (index > 0) {
                    position.moveTowards(knots[index-1])
                }
            }
            visitedPlaces.add(knots.last().copy())
        }
        return visitedPlaces.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
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

    fun moveTowards(position: Position) {
        if (rangeTo(position) > 2) {
            if (x > position.x)
                x--
            else if (x < position.x)
                x++
            if (y > position.y)
                y--
            else if (y < position.y)
                y++
        } else if (x - position.x == 2) {
            x--
        } else if (x - position.x == -2) {
            x++
        } else if (y - position.y == 2) {
            y--
        } else if (y - position.y == -2) {
            y++
        }
    }
}