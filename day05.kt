package day05

import java.io.File
import kotlin.math.absoluteValue

typealias Vec2 = Pair<Int, Int>
typealias Line = Pair<Vec2, Vec2>

operator fun Vec2.plus(other: Vec2) = Vec2(this.first + other.first, this.second + other.second)

operator fun Vec2.minus(other: Vec2) = Vec2(this.first - other.first, this.second - other.second)

operator fun Vec2.div(divisor: Int) = Vec2(this.first / divisor, this.second / divisor)

fun Vec2.absMax(): Int {
    val absFirst = this.first.absoluteValue
    val absSecond = this.second.absoluteValue
    return if (absFirst >= absSecond) absFirst else absSecond
}

fun Line.diffVec() = this.second - this.first

fun Line.slopeVec(): Vec2 {
    val diffVec = this.diffVec()
    return diffVec / diffVec.absMax()
}

fun Line.nPoints() = this.diffVec().absMax() + 1

fun Line.points(): Sequence<Vec2> {
    val nPoints = this.nPoints()
    val slope = this.slopeVec()
    var currCoord = this.first
    return sequence {
        repeat(nPoints) {
            yield(currCoord)
            currCoord += slope
        }
    }
}

fun List<Line>.withoutDiagonals(): List<Line> {
    return this.filter { it.slopeVec().toList().any(0::equals) }
}

fun Map<Vec2, Int>.prettyPrint() {
    val maxX = this.keys.map { it.first }.maxOrNull()
    val maxY = this.keys.map { it.second }.maxOrNull()
    if (maxX == null || maxY == null) return
    for (j in 0..maxY) {
        for (i in 0..maxX) {
            val key = Vec2(i, j)
            print(if (key !in this) '.' else this[key])
        }
        println()
    }
}

fun preProcessData(data: List<String>): List<Line> {
    val lines: MutableList<Line> = mutableListOf()
    for (row in data) {
        val match = Regex("(\\d+),(\\d+) -> (\\d+),(\\d+)").find(row)
        if (match != null) {
            val (x1, y1, x2, y2) = match.destructured.toList().map { it.toInt() }
            lines += Line(Vec2(x1, y1), Vec2(x2, y2))
        }
    }
    return lines
}

fun buildVentMap(lines: List<Line>): Map<Vec2, Int> {
    val ventMap: MutableMap<Vec2, Int> = hashMapOf()
    lines
        .flatMap { it.points() }
        .forEach { coord ->
            ventMap[coord] = if (coord !in ventMap) 1 else ventMap[coord]!!.inc()
        }
    // ventMap.prettyPrint()
    return ventMap
}

fun part1(lines: List<Line>): Int {
    return buildVentMap(lines.withoutDiagonals()).values.count { it >= 2 }
}

fun part2(lines: List<Line>): Int {
    return buildVentMap(lines).values.count { it >= 2 }
}


fun main() {
    val splitData = File("inputs/day05.txt").readLines()
    val data = preProcessData(splitData)
    val answer1 = part1(data)
    println("Answer 1: $answer1") // 4745
    val answer2 = part2(data)
    println("Answer 2: $answer2") // 18442
}