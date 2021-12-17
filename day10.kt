package day10

import java.io.File
import java.util.*

fun preProcessData(lines: List<String>): List<CharArray> {
    return lines.map(String::toCharArray)
}

val bracketPairs = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>',
)
val errorPoints = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137,
)

fun part1(lines: List<CharArray>): Int {
    var points = 0

    for (line in lines) {
        val stack = Stack<Char>()
        for (ch in line) {
            if (ch in bracketPairs.keys) {
                stack.push(ch)
            } else if (ch in bracketPairs.values) {
                val lastCh = stack.pop()
                if (bracketPairs[lastCh] != ch) {
                    points += errorPoints[ch]!!
                    break
                }
            }
        }
    }

    return points
}

val insertionPoints = mapOf(
    ')' to 1,
    ']' to 2,
    '}' to 3,
    '>' to 4,
)

fun part2(lines: List<CharArray>): Long {
    val pointList = mutableListOf<Long>()

    for (line in lines) {
        val stack = Stack<Char>()
        var corrupted = false
        for (ch in line) {
            if (ch in bracketPairs.keys) {
                stack.push(ch)
            } else if (ch in bracketPairs.values) {
                val lastCh = stack.pop()
                if (bracketPairs[lastCh] != ch) {
                    corrupted = true
                    break
                }
            }
        }
        if (!corrupted && !stack.isEmpty()) {
            var score = 0L
            while ((!stack.isEmpty())) {
                score *= 5
                val ch = stack.pop()
                score += insertionPoints[bracketPairs[ch]]!!
            }
            pointList += score
        }
    }
    return pointList.sorted()[pointList.size / 2]
}


fun main() {
    val splitData = File("inputs/day10.txt").readLines()
    val data = preProcessData(splitData)
    val answer1 = part1(data)
    println("Answer 1: $answer1") // 341823
    val answer2 = part2(data)
    println("Answer 2: $answer2") // 2801302861
}