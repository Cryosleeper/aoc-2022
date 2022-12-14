import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        val pairs = input.parse()
        val indexes = mutableListOf<Int>()
        pairs.forEachIndexed { pairIndex, pair ->
            if (pair.first precedes pair.second) indexes.add(pairIndex)
        }

        return indexes.sumOf { it+1 }
    }

    fun part2(input: List<String>): Int {
        val parsed = input.filter { it.isNotEmpty() }.toMutableList()
        parsed.add("[[2]]")
        parsed.add("[[6]]")
        val sorted = parsed.sortedWith { o1, o2 -> if (o1 precedes o2) -1 else 1 }
        println("Sorted list is: \n${sorted.joinToString("\n")}")
        return (sorted.indexOf("[[2]]")+1) * (sorted.indexOf("[[6]]")+1)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}

private fun List<String>.parse(): List<Pair<String, String>> {
    val result = mutableListOf<Pair<String, String>>()
    this.chunked(3) {
        result.add(it[0] to it[1])
    }
    return result
}

private fun Char.isElevenBasedDigit() = this-'0' in 0..10

private infix fun String.precedes(
    that: String
): Boolean {

    fun MutableList<Char>.toStringFormat() = joinToString("").replace(":","10")

    var result = false
    println("Pair ${this} - \n     ${that}")
    run pair@{
        val firstCode = mutableListOf<Char>().also { it.addAll(this.replace("10",":").toList()) }
        val secondCode = mutableListOf<Char>().also { it.addAll(that.replace("10",":").toList()) }
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
                    "\t\tNow comparing ${firstCode.toStringFormat()} - \n" +
                            "\t\t              ${secondCode.toStringFormat()}"
                )
            } else if (firstCode[index].isElevenBasedDigit() && secondCode[index] == '[') {
                println("Right one starts an array, creating array in the left one")
                firstCode.add(index + 1, ']')
                firstCode.add(index, '[')
                println(
                    "\t\tNow comparing ${firstCode.toStringFormat()} - \n" +
                            "\t\t              ${secondCode.toStringFormat()}"
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