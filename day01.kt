package day01

import java.io.File

fun window(levels: List<Int>, windowSize: Int = 2): List<List<Int>> {
    return levels.windowed(windowSize)
}

fun part1(levels: List<Int>): Int {
    return window(levels, windowSize = 2)
        .map { if (it[1] > it[0]) 1 else 0 }
        .sum()
}

fun part2(levels: List<Int>): Int {
    var increases = 0
    var previous : List<Int>? = null
    for (win in window(levels, windowSize = 3)) {
        if (previous == null) {
            previous = win
            continue
        }
        increases += if (win.sum() > previous.sum()) 1 else 0
        previous = win
    }
    return increases
}

fun main() {
    val data = File("inputs/day01.txt").readLines().map { it.toInt() }
    val answer1 = part1(data)
    println("Answer 1: $answer1") // 1688
    val answer2 = part2(data)
    println("Answer 2: $answer2") // 1728
}