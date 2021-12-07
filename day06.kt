package day06

import java.io.File

fun runSimulation(initialFish: List<Int>, timeSteps: Int = 80): Long {
    val fishCounts = LongArray(9)
    initialFish.forEach {
        fishCounts[it]++
    }
    repeat(timeSteps) {
        val nNewFish = fishCounts[0]
        for (i in 1..8) {
            fishCounts[i - 1] = fishCounts[i]
        }
        fishCounts[6] += nNewFish
        fishCounts[8] = nNewFish
    }
    return fishCounts.sum()
}

fun part1(initialFish: List<Int>): Long {
    return runSimulation(initialFish, timeSteps = 80)
}

fun part2(initialFish: List<Int>): Long {
    return runSimulation(initialFish, timeSteps = 256)
}

fun main() {
    val data = File("inputs/day06.txt").readLines()[0]
        .split(",")
        .map(String::toInt)
    val answer1 = part1(data)
    println("Answer 1: $answer1") // 355386
    val answer2 = part2(data)
    println("Answer 2: $answer2") // 1613415325809
}