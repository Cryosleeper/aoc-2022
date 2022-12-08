import kotlin.math.min

private const val DISK_SIZE = 70000000
private const val UPDATE_SPACE_REQUIREMENT = 30000000

fun main() {
    fun part1(input: List<String>): Int {
        val root = input.parse()

        return root.sumOfFoldersUnder100000()
    }

    fun part2(input: List<String>): Int {
        val root = input.parse()
        val spaceToFree = UPDATE_SPACE_REQUIREMENT - (DISK_SIZE - root.size)
        if (spaceToFree <= 0) return 0
        return root.smallestFolderBiggerThan(spaceToFree)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

private fun Folder.smallestFolderBiggerThan(spaceToFree: Int): Int {
    var result = Int.MAX_VALUE
    this.children.forEach {
        if (it is Folder) {
            if (it.size >= spaceToFree) {
                result = min(result, it.size)
                result = min(result, it.smallestFolderBiggerThan(spaceToFree))
            }
        }
    }
    return result
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
    var currentFolder = root

    this.forEach { line ->
        if (line.startsWith("$ cd")) {
            currentFolder = when (line.replace("$ cd ", "")) {
                "/" -> root
                ".." -> currentFolder.parent!!
                else -> {
                    currentFolder.children.find { it.name == line.replace("$ cd ", "") } as Folder
                }
            }
        } else if (line.startsWith("$ ls")) {
            //not useful for anything
        } else {
            val (prefix, name) = line.split(" ")
            val entity = if (prefix == "dir") {
                Folder(name, currentFolder, mutableListOf())
            } else {
                File(name, currentFolder, prefix.toInt())
            }
            currentFolder.children.add(entity)
        }
    }

    return root
}

private abstract class Entry {
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

private class Folder(override val name: String, override val parent: Folder?, val children: MutableList<Entry>): Entry() {
    override val size: Int
        get() = children.sumOf { it.size }
}

private class File(override val name: String, override val parent: Folder?, override val size: Int): Entry()