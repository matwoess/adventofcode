package day12

import java.io.File

fun preProcessData(lines: List<String>): MutableList<Cave> {
    val caves = mutableListOf<Cave>()
    val getOrAddCave: (String) -> Cave = { name: String ->
        var cave = caves.firstOrNull { it.name == name }
        if (cave == null) {
            cave = Cave(name)
            caves += cave
        }
        cave
    }
    for (line in lines) {
        val (from, to) = line.split("-")
        val fromCave = getOrAddCave(from)
        val toCave = getOrAddCave(to)
        fromCave.pathTo += toCave
        toCave.pathTo += fromCave
    }
    return caves
}

data class Cave(val name: String) {
    val pathTo = mutableListOf<Cave>()
    private val size = if (name.uppercase() == name) CaveSize.BIG else CaveSize.SMALL
    val isEnd: Boolean = this.name == "end"
    val isStart: Boolean = this.name == "start"
    val isSmall: Boolean = this.size == CaveSize.SMALL

    enum class CaveSize { BIG, SMALL }
}

fun List<Cave>.hasSmallCaveWithXVisits(visits: Int) =
    this.filter { it.isSmall }.groupingBy { it }.eachCount().filter { it.value == visits }.any()

fun pathsFrom(from: Cave, currPath: MutableList<Cave> = mutableListOf(), maxVisitsSmall: Int = 1): List<List<Cave>> {
    currPath += from
    val paths = mutableListOf<List<Cave>>()
    for (next in from.pathTo) {
        if (next.isStart) continue
        if (next.isSmall && next in currPath) {
            if (maxVisitsSmall == 1 || currPath.hasSmallCaveWithXVisits(maxVisitsSmall)) continue
        }
        if (next.isEnd) {
            paths += currPath.toList() + next
        } else {
            paths.addAll(pathsFrom(next, currPath.toMutableList(), maxVisitsSmall))
        }
    }
    return paths
}

fun buildPaths(caves: List<Cave>, maxVisitsSmall: Int = 1): List<List<Cave>> {
    val start = caves.first(Cave::isStart)
    return pathsFrom(start, maxVisitsSmall = maxVisitsSmall)
}

fun printPaths(paths: List<List<Cave>>) {
    for (path in paths) {
        println(path.joinToString(",") { it.name })
    }
}

fun part1(caves: List<Cave>): Int {
    val paths = buildPaths(caves)
    // printPaths(paths)
    return paths.size
}

fun part2(caves: List<Cave>): Int {
    val paths = buildPaths(caves, maxVisitsSmall = 2)
    // printPaths(paths)
    return paths.size
}


fun main() {
    val splitData = File("inputs/day12.txt").readLines()
    val data = preProcessData(splitData)
    val answer1 = part1(data)
    println("Answer 1: $answer1") // 3887
    val answer2 = part2(data)
    println("Answer 2: $answer2") // 104834
}