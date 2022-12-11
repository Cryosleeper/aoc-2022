import kotlin.math.abs

private const val SCREEN_WIDTH = 40

fun main() {
    fun part1(input: List<String>): Int {
        var registerX = 1
        var currentCycle = 1
        val valuesToSave = mutableMapOf<Int, Int?>(
            20 to null,
            60 to null,
            100 to null,
            140 to null,
            180 to null,
            220 to null
        )
        input.forEach { line ->
            valuesToSave.performCheck(currentCycle, registerX)
            val command = line.split(" ")
            when (command[0]) {
                "noop" -> currentCycle++
                "addx" -> {
                    currentCycle += 1
                    valuesToSave.performCheck(currentCycle, registerX)
                    currentCycle += 1
                    registerX += command[1].toInt()
                }
            }
        }
        return valuesToSave.values.sumOf { it!! }
    }

    fun part2(input: List<String>): String {
        var output = ""
        var registerX = 1
        var currentCycle = 0
        input.forEach { line ->
            output += getPixel(currentCycle, registerX)
            val command = line.split(" ")
            when (command[0]) {
                "noop" -> currentCycle++
                "addx" -> {
                    currentCycle += 1
                    output += getPixel(currentCycle, registerX)
                    currentCycle += 1
                    registerX += command[1].toInt()
                }
            }
        }
        return output.drawOnScreen()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    check(part2(testInput) == """
        ##..##..##..##..##..##..##..##..##..##..
        ###...###...###...###...###...###...###.
        ####....####....####....####....####....
        #####.....#####.....#####.....#####.....
        ######......######......######......####
        #######.......#######.......#######.....""".trimIndent())

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

private fun MutableMap<Int, Int?>.performCheck(currentCycle: Int, registerX: Int) {
    keys.forEach { key ->
        if (currentCycle == key && this[key] == null) {
            this[key] = registerX * currentCycle
            return
        }
    }
}

private fun getPixel(pixel:Int, signal: Int) = if (abs(pixel%SCREEN_WIDTH - signal) < 2) "#" else "."

private fun String.drawOnScreen() = toList().chunked(SCREEN_WIDTH).joinToString("\n") { it.joinToString("") }
