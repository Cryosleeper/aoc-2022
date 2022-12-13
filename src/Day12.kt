fun main() {
    fun part1(input: List<String>): Int {
        val (start, end, heightMap) = input.parse()
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
                    if (visited.at(potentialPlace) <0 && (heightMap.at(place)- heightMap.at(potentialPlace)) <= 1) {
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
                //heightMap.map { it.map { if (it >= 0) it/3 else "+" } }.forEach { println(it.joinToString("")) }
                val roundColor = "\u001B[34m"
                val heightColor = "\u001B[31m"
                val colorBreak = "\u001B[0m"
                visited.mapIndexed { row, line -> line.mapIndexed { column, place -> String.format("[$roundColor%3d$colorBreak - $heightColor${'a'+heightMap.at(row to column)}$colorBreak]", place) } }.forEach { println(it.joinToString("")) }
                throw Exception("No path found")
            }
        }

        return visited.at(start)
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    //check(part2(testInput) == 0)

    val input = readInput("Day12")
    println(part1(input))
    //println(part2(input))
}

private fun List<String>.parse(): Triple<Pair<Int, Int>, Pair<Int, Int>, List<List<Int>>> {
    val inputToParse = this.map { "0${it}0" }.toMutableList().apply {
        add(0, this[0].map { "0" }.joinToString(""))
        add(this[0].map { "0" }.joinToString(""))
    }
    var start: Pair<Int, Int> = 0 to 0
    var end: Pair<Int, Int> = 0 to 0
    val map = inputToParse.mapIndexed { rowIndex, row -> row.toList().mapIndexed { columnIndex, char ->
        if (char == 'S') {
            start = rowIndex to columnIndex
            0
        } else if (char == 'E') {
            end = rowIndex to columnIndex
            'z'- 'a'
        } else {
            char - 'a'
        }
    } }

    return Triple(start, end, map)
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