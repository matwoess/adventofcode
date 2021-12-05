package day03

import java.io.File

fun List<String>.bitsAtIndex(i: Int): List<Char> =
    this.map { it[i] }

fun List<Char>.frequencyTuple(): Pair<Int, Int> {
    val count0 = this.count { it == '0' }
    val count1 = this.size - count0
    return Pair(count0, count1)
}

fun getPowerConsumption(numbers: List<String>, gammaRate: Boolean = true): Int {
    val nBits = numbers[0].length
    var mostCommonBits = ""
    for (b in 0 until nBits) {
        val (count0, count1) = numbers.bitsAtIndex(b).frequencyTuple()
        val (mostCommonBit, leastCommonBit) = if (count0 > count1) Pair('1', '0') else Pair('0', '1')
        mostCommonBits += if (gammaRate) mostCommonBit else leastCommonBit
    }
    return mostCommonBits.toInt(2)
}

fun getLifeSupport(numbers: List<String>, co2: Boolean = false): Int {
    var numbers = numbers.toList()
    val nBits = numbers.first().length
    for (b in 0 until nBits) {
        val (count0, count1) = numbers.bitsAtIndex(b).frequencyTuple()
        val filterFor = if (count0 <= count1) {
            if (!co2) '1' else '0'
        } else {
            if (!co2) '0' else '1'
        }
        numbers = numbers.filter { it[b] == filterFor }
        if (numbers.size == 1) break
    }
    return numbers[0].toInt(2)
}

fun part1(numbers: List<String>): Int {
    val gammaRate = getPowerConsumption(numbers)
    val epsilonRate = getPowerConsumption(numbers, gammaRate = false)
    return gammaRate * epsilonRate
}

fun part2(numbers: List<String>): Int {
    val oxygen = getLifeSupport(numbers)
    val co2 = getLifeSupport(numbers, co2 = true)
    return oxygen * co2
}

fun main() {
    val data = File("inputs/day03.txt").readLines()
    val answer1 = part1(data)
    println("Answer 1: $answer1") // 2972336
    val answer2 = part2(data)
    println("Answer 2: $answer2") // 3368358
}