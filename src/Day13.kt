import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        val pairs = input.parse()
        val indexes = mutableListOf<Int>()
        pairs.forEachIndexed { pairIndex, pair ->
            if (comparePair(pair)) indexes.add(pairIndex)
        }

        return indexes.sumOf { it+1 }
    }

    fun part2(input: List<String>): Int {
        val parsed = input.filter { it.isNotEmpty() }.map { it.replace("10", ":").toMutableList() }.toMutableList()
        parsed.add("[[2]]".toMutableList())
        parsed.add("[[6]]".toMutableList())
        val sorted = parsed.sortedWith { o1, o2 -> if (comparePair(o1 to o2)) -1 else 1 }
        println("Sorted list is ${sorted.map { it.joinToString("", postfix = "\n") }}")
        return (sorted.indexOf("[[2]]".toList())+1) * (sorted.indexOf("[[6]]".toList())+1)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}

private fun List<String>.parse(): List<Pair<MutableList<Char>, MutableList<Char>>> {
    val result = mutableListOf<Pair<MutableList<Char>, MutableList<Char>>>()
    this.chunked(3) {
        result.add(it[0].replace("10", ":").toMutableList() to it[1].replace("10",":").toMutableList())
    }
    return result
}

private fun Char.isElevenBasedDigit() = this-'0' in 0..10

fun comparePair(
    pair: Pair<MutableList<Char>, MutableList<Char>>
): Boolean {
    var result = false
    println("Pair ${pair.first.joinToString("")} - \n     ${pair.second.joinToString("")}")
    run pair@{
        val firstCode = mutableListOf<Char>().apply { addAll(pair.first) }
        val secondCode = mutableListOf<Char>().apply { addAll(pair.second) }
        (0 until max(firstCode.size, secondCode.size)).forEach { index ->
            print("\tPosition $index: ${firstCode[index]}-${secondCode[index]}:")
            if (firstCode[index] == '[' && secondCode[index] == '[') {
                println("Starting arrays in both")
            } else if (firstCode[index] == ']' && secondCode[index] == ']') {
                println("Closing arrays in both")
            } else if (firstCode[index] == '[' && secondCode[index].isElevenBasedDigit()) {
                println("Left one starts an array, creating array in the right one")
                secondCode.add(index + 1, ']')
                secondCode.add(index, '[')
                println(
                    "\t\tNow comparing ${firstCode.joinToString("")} - \n" +
                            "\t\t              ${secondCode.joinToString("")}"
                )
            } else if (firstCode[index].isElevenBasedDigit() && secondCode[index] == '[') {
                println("Right one starts an array, creating array in the left one")
                firstCode.add(index + 1, ']')
                firstCode.add(index, '[')
                println(
                    "\t\tNow comparing ${firstCode.joinToString("")} - \n" +
                            "\t\t              ${secondCode.joinToString("")}"
                )
            } else if (firstCode[index] == ']' && secondCode[index] != ']') {
                println("Left one closes an array, sequence is correct")
                result = true
                return@pair
            } else if (firstCode[index] != ']' && secondCode[index] == ']') {
                println("Right one closes an array, sequence is wrong")
                return@pair
            } else if (firstCode[index].isElevenBasedDigit() && secondCode[index].isElevenBasedDigit()) {
                println("Comparing ${firstCode[index]} and ${secondCode[index]}")
                val first = firstCode[index] - '0'
                val second = secondCode[index] - '0'
                if (first < second) {
                    println("\tCorrect sequence")
                    result = true
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
            if (index == firstCode.size - 1) {
                println("\tLeft one is shorter, sequence is correct")
                result = true
                return@pair
            } else if (index == secondCode.size - 1) {
                println("\tRight one is shorter, sequence is wrong")
                return@pair
            }
        }
    }
    return result
}