fun main() {
    fun part1(input: List<String>): Int {
        val root = input.parse()

        return root.sumOfFoldersUnder100000()
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    //check(part2(testInput) == 0)

    val input = readInput("Day07")
    println(part1(input))
    //println(part2(input))
}

private fun Folder.sumOfFoldersUnder100000(): Int {
    var sum = 0
    this.children.forEach {
        if (it is Folder) {
            if (it.size < 100000) {
                sum += it.size
            }
            sum += it.sumOfFoldersUnder100000()
        }
    }
    return sum
}

private fun List<String>.parse(): Folder {
    val root = Folder("/", null, mutableListOf())
    var currentEntity = root

    this.forEach { line ->
        if (line.startsWith("$ cd")) {
            currentEntity = when (line.replace("$ cd ", "")) {
                "/" -> root
                ".." -> currentEntity.parent!!
                else -> {
                    currentEntity.children.find { it.name == line.replace("$ cd ", "") } as Folder
                }
            }
        } else if (line.startsWith("$ ls")) {
            //prepare to update currentEntity
        } else {
            if (line.startsWith("dir")) {
                val folder = Folder(line.replace("dir ", ""), currentEntity, mutableListOf())
                currentEntity.children.add(folder)
            } else {
                val (size, name) = line.split(" ")
                val file = File(name, currentEntity, size.toInt())
                currentEntity.children.add(file)
            }
        }
    }

    return root
}

abstract class Entry {
    abstract val name: String
    abstract val parent: Folder?
    abstract val size: Int

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Entry) return false

        if (name != other.name) return false
        if (parent != other.parent) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (parent?.hashCode() ?: 0)
        return result
    }
}

class Folder(override val name: String, override val parent: Folder?, val children: MutableList<Entry>): Entry() {
    override val size: Int
        get() = children.sumOf { it.size }
}

class File(override val name: String, override val parent: Folder?, override val size: Int): Entry()