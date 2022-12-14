import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        val pairs = input.parse()
        val indexes = mutableListOf<Int>()
        pairs.forEachIndexed { pairIndex, pair ->
            println("Pair ${pair.first.joinToString("")} - \n     ${pair.second.joinToString("")}")
            run pair@{
                (0 until max(pair.first.size, pair.second.size)).forEach { index ->
                    print("\tPosition $index: ${pair.first[index]}-${pair.second[index]}:")
                    if (pair.first[index] == '[' && pair.second[index] == '[') {
                        println("Starting arrays in both")
                    } else if (pair.first[index] == ']' && pair.second[index] == ']') {
                        println("Closing arrays in both")
                    } else if (pair.first[index] == '[' && pair.second[index].isElevenBasedDigit()) {
                        println("Left one starts an array, creating array in the right one")
                        pair.second.add(index + 1, ']')
                        pair.second.add(index, '[')
                        println("\t\tNow comparing ${pair.first.joinToString("")} - \n" +
                                "\t\t              ${pair.second.joinToString("")}")
                    } else if (pair.first[index].isElevenBasedDigit() && pair.second[index] == '[') {
                        println("Right one starts an array, creating array in the left one")
                        pair.first.add(index + 1, ']')
                        pair.first.add(index, '[')
                        println("\t\tNow comparing ${pair.first.joinToString("")} - \n" +
                                "\t\t              ${pair.second.joinToString("")}")
                    } else if (pair.first[index] == ']' && pair.second[index] != ']') {
                        println("Left one closes an array, sequence is correct")
                        indexes.add(pairIndex)
                        return@pair
                    } else if (pair.first[index] != ']' && pair.second[index] == ']') {
                        println("Right one closes an array, sequence is wrong")
                        return@pair
                    } else if (pair.first[index].isElevenBasedDigit() && pair.second[index].isElevenBasedDigit()) {
                        println("Comparing ${pair.first[index]} and ${pair.second[index]}")
                        val first = pair.first[index] - '0'
                        val second = pair.second[index] - '0'
                        if (first < second) {
                            println("\tCorrect sequence")
                            indexes.add(pairIndex)
                            return@pair
                        } else if (first > second) {
                            println("\tWrong sequence")
                            return@pair
                        } else {
                            println("\tSame digit, continuing")
                        }
                    } else {
                        println("Skipping coma")
                    }
                    if (index == pair.first.size - 1) {
                        println("\tLeft one is shorter, sequence is correct")
                        indexes.add(pairIndex)
                        return@pair
                    } else if (index == pair.second.size - 1) {
                        println("\tRight one is shorter, sequence is wrong")
                        return@pair
                    }
                }
            }
        }

        return indexes.sumOf { it+1 }
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    //check(part2(testInput) == 0)

    val input = readInput("Day13")
    println(part1(input))
    //println(part2(input))
}

private fun List<String>.parse(): List<Pair<MutableList<Char>, MutableList<Char>>> {
    val result = mutableListOf<Pair<MutableList<Char>, MutableList<Char>>>()
    this.chunked(3) {
        result.add(it[0].replace("10", ":").toMutableList() to it[1].replace("10",":").toMutableList())
    }
    return result
}

private fun Char.isElevenBasedDigit() = this-'0' in 0..10