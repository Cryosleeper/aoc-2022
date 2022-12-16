import kotlin.math.abs

fun main() {
    fun part1(input: List<String>, yToUse: Int): Int {
        val sensors = input.parseToSensors()
        val pointsToIgnore = mutableSetOf<Beacon>()
        val minX = sensors.minBy { it.x }.x
        val maxX = sensors.maxBy { it.x }.x
        val maxDistance = sensors.maxBy { it.distance }.distance
        ((minX - maxDistance)..(maxX + maxDistance)).forEach { x ->
            for (sensor in sensors) {
                if (sensor.distanceTo(x, yToUse) <= sensor.distance) {
                    pointsToIgnore.add(Beacon(x, yToUse))
                    break
                }
            }
        }
        pointsToIgnore.removeAll(sensors.map { it.beacon }.toSet())
        println("Points to ignore: $pointsToIgnore")
        return pointsToIgnore.size
    }

    fun part2(input: List<String>): Int {
        TODO()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput, 10) == 26)
    //check(part2(testInput) == 0)

    val input = readInput("Day15")
    println(part1(input, 2000000))
    //println(part2(input))
}

private fun List<String>.parseToSensors(): List<Sensor> {
    val result = mutableListOf<Sensor>()
    this.forEach {line ->
        line.split(":").apply {
            val (sx, sy) = "-?\\d+".toRegex().findAll(this[0]).toList().subList(0, 2).map { it.value.toInt() }
            val (bx, by) = "-?\\d+".toRegex().findAll(this[1]).toList().subList(0, 2).map { it.value.toInt() }
            result.add(Sensor(sx, sy, Beacon(bx, by)))
        }
    }
    println("Parsed sensors are $result")
    return result
}

private data class Beacon(val x: Int, val y: Int)
private data class Sensor(val x: Int, val y: Int, val beacon: Beacon) {
    val distance: Int by lazy { distanceTo(beacon.x, beacon.y) }
    fun distanceTo(x: Int, y: Int) = abs(this.x - x) + abs(this.y - y)
}