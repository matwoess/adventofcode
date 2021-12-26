package day14

import java.io.File

typealias CharPair = Pair<Char, Char>

fun preProcessData(lines: List<String>): Triple<Map<CharPair, Char>, Map<CharPair, Long>, Char> {
    val polymerTemplate = lines[0]
    val insertionRules = mutableMapOf<CharPair, Char>()
    for (line in lines.subList(2, lines.size)) {
        val (from, to) = line.split(" -> ")
        val fromPair = CharPair(from[0], from[1])
        val toChar = to[0]
        insertionRules[fromPair] = toChar
    }
    val pairCounts = mutableMapOf<CharPair, Long>()
    for ((k, v) in polymerTemplate.windowed(2).map { CharPair(it[0], it[1]) }.groupingBy { it }.eachCount()) {
        pairCounts[k] = v.toLong()
    }
    val lastChar = polymerTemplate.last()
    return Triple(insertionRules.toMap(), pairCounts.toMap(), lastChar)
}

fun applyRules(insertionRules: Map<CharPair, Char>, pairCounts: Map<CharPair, Long>): Map<CharPair, Long> {
    val newPairCounts = mutableMapOf<CharPair, Long>().withDefault { 0L }
    for ((pair, counts) in pairCounts) {
        val insertion: Char = insertionRules[pair] ?: error("no rule for pair $pair!")
        val firstPair = CharPair(pair.first, insertion)
        val secondPair = CharPair(insertion, pair.second)
        newPairCounts[firstPair] = newPairCounts.getValue(firstPair) + counts
        newPairCounts[secondPair] = newPairCounts.getValue(secondPair) + counts
    }
    return newPairCounts.toMap()
}

fun pairCountsToOccurrences(pairCounts: Map<CharPair, Long>, lastChar: Char): Map<Char, Long> {
    val occurrences = mutableMapOf(lastChar to 1L).withDefault { 0L }
    pairCounts.forEach { (pair, count) -> occurrences[pair.first] = occurrences.getValue(pair.first) + count }
    return occurrences
}

fun polymerization(
    insertionRules: Map<CharPair, Char>,
    pairCounts: Map<CharPair, Long>,
    lastChar: Char,
    repetitions: Int = 10
): Long {
    var currPairCounts = pairCounts
    repeat(repetitions) {
        currPairCounts = applyRules(insertionRules, currPairCounts)
    }
    val occurrences = pairCountsToOccurrences(currPairCounts, lastChar)
    return occurrences.values.maxOrNull()!! - occurrences.values.minOrNull()!!
}

fun part1(insertionRules: Map<CharPair, Char>, pairCounts: Map<CharPair, Long>, lastChar: Char): Long {
    return polymerization(insertionRules, pairCounts, lastChar)
}

fun part2(insertionRules: Map<CharPair, Char>, pairCounts: Map<CharPair, Long>, lastChar: Char): Long {
    return polymerization(insertionRules, pairCounts, lastChar, repetitions = 40)
}


fun main() {
    val splitData = File("inputs/day14.txt").readLines()
    val (insertionRules, pairCounts, lastChar) = preProcessData(splitData)
    val answer1 = part1(insertionRules, pairCounts, lastChar)
    println("Answer 1: $answer1") // 3284
    val answer2 = part2(insertionRules, pairCounts, lastChar)
    println("Answer 2: $answer2") // 4302675529689
}