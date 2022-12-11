fun main() {
    fun part1(input: List<String>): Int {
        val monkeys: Map<String, Monkey> = input.chunked(7) {
            val monkey = Monkey(it)
            monkey.id to monkey
        }.toMap()
        repeat(20) {
            println("### ROUND $it BEGINS! ###\n")
            monkeys.forEach { it.value.inspectAndThrowToOtherMonkeys(monkeys) }
        }
        return monkeys.values.map { it.thrownItems }.sortedByDescending { it }.run { get(0) * get(1) }
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605)
    //check(part2(testInput) == 0)

    val input = readInput("Day11")
    println(part1(input))
    //println(part2(input))
}


private class Monkey(input: List<String>) {

    val id: String
    private var _thrownItems = 0
    val thrownItems: Int
        get() = _thrownItems
    private val items: MutableList<Item>
    private val worryModifier: WorryModifier
    private val strategy: (Item) -> String

    init {
        id = "\\d+".toRegex().find(input[0])!!.value
        items = "\\d+".toRegex().findAll(input[1]).mapIndexed { index, matchResult -> Item("$id $index", matchResult.value.toInt()) }.toMutableList()

        val (first, second, operator) = readWorryModifiers(input[2])
        worryModifier = WorryModifier(first, second, operator)

        val divider = "\\d+".toRegex().find(input[3])!!.value.toInt()
        val trueMonkey = "\\d+".toRegex().find(input[4])!!.value
        val falseMonkey = "\\d+".toRegex().find(input[5])!!.value
        strategy = {
            if (it.worryLevel % divider == 0) trueMonkey else falseMonkey
        }
    }

    private fun readWorryModifiers(input: String): Triple<Int?, Int?, WorryOperator> {
        val worryModifications = input.substring(input.indexOf("=") + 2).split(" ")
        val first: Int? = when (worryModifications[0]) {
            "old" -> null
            else -> worryModifications[0].toInt()
        }
        val second: Int? = when (worryModifications[2]) {
            "old" -> null
            else -> worryModifications[2].toInt()
        }
        val operator = WorryOperator.from(worryModifications[1])
        return Triple(first, second, operator)
    }

    fun inspectAndThrowToOtherMonkeys(monkeys: Map<String, Monkey>) {
        println("Monkey $id is inspecting items ${items.map { "(${it.id}, ${it.worryLevel})" }.joinToString(", ")}")
        items.forEach { item ->
            println("\tMonkey $id is inspecting item ${item.id} with worry level ${item.worryLevel}")
            item.worryLevel = worryModifier.modify(item.worryLevel)
            println("\t\tItem ${item.id} has worry level ${item.worryLevel} now")
            monkeys[strategy(item).also { println("\t\tMonkey $id throws item ${item.id} to monkey $it") }]!!.receiveItem(item)
            _thrownItems++
        }
        println("\tMonkey $id has thrown $thrownItems items")
        println()
        items.clear()
    }

    private fun receiveItem(item: Item) {
        items.add(item)
    }
}

private class WorryModifier (private val first: Int?, private val second: Int?, private val operator: WorryOperator) {
    fun modify(currentWorry: Int): Int {
        return (operator.perform(first ?: currentWorry, second ?: currentWorry)) / 3
    }
}

private enum class WorryOperator {
    Add {
        override fun perform(first: Int, second: Int) = first + second
    }, Multiply {
        override fun perform(first: Int, second: Int) = first * second
    };

    abstract fun perform(first: Int, second: Int): Int

    companion object {
        fun from(value: String) = when(value) {
            "+" -> Add
            "*" -> Multiply
            else -> throw IllegalArgumentException("Unknown operator: $value")
        }
    }
}

private data class Item(val id: String, var worryLevel: Int)