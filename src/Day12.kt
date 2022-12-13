private val ROUND_COLOR = "\u001B[34m"
private val HEIGHT_COLOR = "\u001B[31m"
private val COLOR_BREAK = "\u001B[0m"

fun main() {
    fun part1(input: List<String>): Int {
        val (start, end, heightMap) = input.parse()

        val visited = calculatePath(end, start, heightMap)

        return visited.at(start)
    }

    fun part2(input: List<String>): Int {
        val (start, end, heightMap) = input.parse()

        val visited = calculatePath(end, start, heightMap)

        var min = visited.at(start)

        heightMap.indices.forEach { row ->
            heightMap[0].indices.forEach { column ->
                if (heightMap.at(row to column) == 0) {
                    if (visited.at(row to column) in 1..min) {
                        min = visited.at(row to column)
                    }
                }
            }
        }

        return min
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}

private fun List<String>.parse(): Triple<Pair<Int, Int>, Pair<Int, Int>, List<List<Int>>> {
    val inputToParse = this.map { "0${it}0" }.toMutableList().apply {
        add(0, this[0].map { "0" }.joinToString(""))
        add(this[0].map { "0" }.joinToString(""))
    }
    var start: Pair<Int, Int> = 0 to 0
    var end: Pair<Int, Int> = 0 to 0
    val map = inputToParse.mapIndexed { rowIndex, row ->
        row.toList().mapIndexed { columnIndex, char ->
            if (char == 'S') {
                start = rowIndex to columnIndex
                0
            } else if (char == 'E') {
                end = rowIndex to columnIndex
                'z' - 'a'
            } else {
                char - 'a'
            }
        }
    }

    map.map { it.map { "${it.toColor()}*$COLOR_BREAK" } }.forEach { println(it.joinToString("")) }

    return Triple(start, end, map)
}

private fun Int.toColor(): String {
    if (this < 0) {
        return ""
    }
    return listOf<String>(
        "\u001b[38;5;27m",
        "\u001b[38;5;26m",
        "\u001b[38;5;25m",
        "\u001b[38;5;24m",
        "\u001b[38;5;28m",
        "\u001b[38;5;64m",
        "\u001b[38;5;100m",
        "\u001b[38;5;136m",
        "\u001b[38;5;172m",
        "\u001b[38;5;208m",
        "\u001b[38;5;220m"
    )[this/3]
    /*return listOf<String>(
        "\u001b[38;5;28m",
        "\u001b[38;5;232m",
        "\u001b[38;5;233m",
        "\u001b[38;5;234m",
        "\u001b[38;5;235m",
        "\u001b[38;5;236m",
        "\u001b[38;5;237m",
        "\u001b[38;5;238m",
        "\u001b[38;5;239m",
        "\u001b[38;5;240m",
        "\u001b[38;5;241m",
        "\u001b[38;5;242m",
        "\u001b[38;5;243m",
        "\u001b[38;5;244m",
        "\u001b[38;5;245m",
        "\u001b[38;5;246m",
        "\u001b[38;5;247m",
        "\u001b[38;5;248m",
        "\u001b[38;5;249m",
        "\u001b[38;5;250m",
        "\u001b[38;5;251m",
        "\u001b[38;5;252m",
        "\u001b[38;5;253m",
        "\u001b[38;5;254m",
        "\u001b[38;5;255m",
        "\u001b[38;5;220m"
    )[this]*/
}

private fun List<List<Int>>.at(position: Pair<Int, Int>) = this[position.first][position.second]
private fun MutableList<MutableList<Int>>.setAt(position: Pair<Int, Int>, value: Int) {
    this[position.first][position.second] = value
}

private val Pair<Int, Int>.toLeft: Pair<Int, Int>
    get() = first to second - 1
private val Pair<Int, Int>.toRight: Pair<Int, Int>
    get() = first to second + 1

private val Pair<Int, Int>.above: Pair<Int, Int>
    get() = first - 1 to second

private val Pair<Int, Int>.below: Pair<Int, Int>
    get() = first + 1 to second

fun calculatePath(
    end: Pair<Int, Int>,
    start: Pair<Int, Int>,
    heightMap: List<List<Int>>
): List<List<Int>> {
    val visited: MutableList<MutableList<Int>> = heightMap.indices.map {
        heightMap[0].indices.map { -1 }.toMutableList()
    }.toMutableList()

    visited.setAt(end, 0)

    var round = 1
    val placesToCheck = mutableListOf(end)
    val placesToCheckNextRound = mutableListOf<Pair<Int, Int>>()
    while (visited.at(start) < 0) {
        println("Places to check this round: $placesToCheck")
        placesToCheck.forEach { place ->
            listOf(place.toLeft, place.toRight, place.above, place.below).forEach { potentialPlace ->
                if (visited.at(potentialPlace) < 0 && (heightMap.at(place) - heightMap.at(potentialPlace)) <= 1) {
                    visited.setAt(potentialPlace, round)
                    placesToCheckNextRound.add(potentialPlace)
                }
            }
        }
        placesToCheck.clear()
        placesToCheck.addAll(placesToCheckNextRound)
        placesToCheckNextRound.clear()
        round++

        if (placesToCheck.size == 0 && visited.at(start) < 0) {
            visited.mapIndexed { row, line ->
                line.mapIndexed { column, place ->
                    String.format(
                        "[$ROUND_COLOR%3d$COLOR_BREAK - $HEIGHT_COLOR${
                            'a' + heightMap.at(
                                row to column
                            )
                        }$COLOR_BREAK]", place
                    )
                }
            }.forEach { println(it.joinToString("")) }
            throw Exception("No path found")
        }
    }

    return visited
}