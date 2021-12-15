package day09

import java.io.File


typealias Coord = Pair<Int, Int>

fun preProcessData(lines: List<String>): HeightMap {
    val array = lines.map { it.map(Char::digitToInt) }
    return HeightMap(array.toList())
}

operator fun List<List<Int>>.get(coord: Coord) = this[coord.first][coord.second]

class HeightMap(val array: List<List<Int>>) {
    private val maxX = array.size
    private val maxY = array.maxOf { it.size }

    fun getLowPoints(): List<Coord> {
        val lowPoints = mutableListOf<Coord>()
        for (x in 0 until maxX) {
            for (y in 0 until maxY) {
                val coord = Coord(x, y)
                val cellVal = array[coord]
                if (coord.getNeighbors().map { array[it] }.all { it > cellVal }) {
                    lowPoints.add(coord)
                }
            }
        }
        return lowPoints.toList()
    }

    fun getBasins(): List<List<Coord>> {
        return getLowPoints().map { listOf(it) + higherNeighbors(it) }
    }

    private fun higherNeighbors(c: Coord): List<Coord> {
        val neighbors = c.getNeighbors().filter { array[it] != 9 && array[it] > array[c] }
        return if (neighbors.isEmpty()) {
            neighbors
        } else {
            (neighbors + neighbors.flatMap { higherNeighbors(it) }).distinct()
        }
    }

    private fun Coord.getNeighbors(): List<Coord> {
        val i = this.first
        val j = this.second
        val neighbors = mutableListOf<Coord>()
        if (i > 0) neighbors += Coord(i - 1, j)
        if (i < maxX - 1) neighbors += Coord(i + 1, j)
        if (j > 0) neighbors += Coord(i, j - 1)
        if (j < maxY - 1) neighbors += Coord(i, j + 1)
        return neighbors
    }
}

fun part1(heightMap: HeightMap): Int {
    return heightMap.getLowPoints()
        .map { c -> heightMap.array[c] }
        .sumOf { it + 1 }
}

fun part2(heightMap: HeightMap): Int {
    return heightMap.getBasins()
        .map { it.count() }
        .sorted()
        .takeLast(3)
        .fold(1) { acc, next -> acc * next }
}


fun main() {
    val fileData = File("inputs/day09.txt").readLines()
    val processedData = preProcessData(fileData)
    val answer1 = part1(processedData)
    println("Answer 1: $answer1") // 448
    val answer2 = part2(processedData)
    println("Answer 2: $answer2") // 1417248
}