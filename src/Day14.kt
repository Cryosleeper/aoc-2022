fun main() {
    fun part1(input: List<String>): Int {
        val parsedInput = input.parseToStone().map { it.points }
        val stones = parsedInput.flatten().toSet()
        val maxDepth = stones.maxByOrNull { it.y }!!.y
        println("Cave is $stones with max depth of $maxDepth")
        val sand = mutableSetOf<Point>()
        var activeSand = Point(500, 0)
        run sand@{
            while (sand.contains(Point(500, 0)).not()) {
                while (activeSand.canFall(stones + sand)) {
                    activeSand.fall(stones + sand)
                    if (activeSand.y > maxDepth) {
                        println("Sand falls off at $activeSand")
                        return@sand
                    }
                }
                println("Sand stopped at $activeSand")
                sand.add(activeSand)
                activeSand = Point(500, 0)
            }
        }
        return sand.size
    }

    fun part2(input: List<String>): Int {
        val parsedInput = input.parseToStone().map { it.points }
        val stones = parsedInput.flatten().toSet()
        val maxDepth = stones.maxByOrNull { it.y }!!.y
        println("Cave is $stones with max depth of $maxDepth")
        val sand = mutableSetOf<Point>()
        var activeSand = Point(500, 0)
        run sand@{
            while (sand.contains(Point(500, 0)).not()) {
                while (activeSand.canFall(stones + sand, maxDepth+2)) {
                    activeSand.fall(stones + sand, maxDepth+2)
                }
                println("Sand stopped at $activeSand")
                sand.add(activeSand)
                activeSand = Point(500, 0)
            }
        }
        return sand.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    //println(part1(input))
    println(part2(input))
}

private fun List<String>.parseToStone(): List<Stone> {
    val result = mutableListOf<Stone>()
    this.forEach {line ->
        result.add(Stone(line.split(" -> ").map { point -> point.split(",").let { Point(it[0].toInt(), it[1].toInt()) } }))
    }
    return result
}

private data class Stone(private val turnPoints: List<Point>) {
    val points: Set<Point> = turnPoints.convertToPoints()

    private fun List<Point>.convertToPoints(): Set<Point> {
        val result = mutableSetOf<Point>()
        repeat(this.size - 1) { index ->
            range(this[index].x, this[index+1].x).forEach { x ->
                range(this[index].y, this[index+1].y).forEach { y ->
                    //println("Adding stone to $x, $y")
                    result.add(Point(x, y))
                }
            }
        }
        return result
    }

    private fun range(from: Int, to: Int) = if (from <= to) from..to else to..from
}

private data class Point(var x: Int, var y: Int) {
    override fun toString(): String {
        return "($x,$y)"
    }

    fun canFall(objects: Set<Point>, limit: Int = Int.MAX_VALUE): Boolean {
        return (limit > y+1) && (objects.contains(Point(x, y+1)).not() || objects.contains(Point(x-1, y+1)).not() || objects.contains(Point(x+1, y+1)).not())
    }

    fun fall(objects: Set<Point>, limit: Int = Int.MAX_VALUE): Boolean {
        if (y+1 == limit) {
            return false
        } else if (objects.contains(Point(x, y+1)).not()) {
            y += 1
            return true
        } else if (objects.contains(Point(x-1, y+1)).not()) {
            x -= 1
            y += 1
            return true
        } else if (objects.contains(Point(x+1, y+1)).not()) {
            x += 1
            y += 1
            return true
        } else {
            return false
        }
    }
}