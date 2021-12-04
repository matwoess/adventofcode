package day02

import java.io.File

enum class Kind {
    FORWARD, UP, DOWN
}

data class Command(
    val kind: Kind,
    val arg: Int
)

fun String.toCommand(): Command {
    val (kindStr, argStr) = this.split(' ')
    val kind: Kind = when (kindStr) {
        "forward" -> Kind.FORWARD
        "up" -> Kind.UP
        "down" -> Kind.DOWN
        else -> throw IllegalArgumentException("unknown command type: $kindStr")
    }
    val arg: Int = argStr.toInt()
    return Command(kind, arg)
}

data class State(
    var horizontal: Int = 0,
    var depth: Int = 0,
    var aim: Int = -1
) {
    fun applyCommand(cmd: Command) {
        if (this.aim == -1) {
            when (cmd.kind) {
                Kind.FORWARD -> this.horizontal += cmd.arg
                Kind.DOWN -> this.depth += cmd.arg
                Kind.UP -> this.depth -= cmd.arg
            }
        } else {
            when (cmd.kind) {
                Kind.FORWARD -> {
                    this.horizontal += cmd.arg
                    this.depth += this.aim * cmd.arg
                }
                Kind.DOWN -> this.aim += cmd.arg
                Kind.UP -> this.aim -= cmd.arg
            }
        }
    }

}

fun part1(commands: List<Command>): Int {
    val state = State(0, 0)
    commands.forEach { state.applyCommand(it) }
    return state.horizontal * state.depth
}

fun part2(commands: List<Command>): Int {
    val state = State(0, 0, 0)
    commands.forEach { state.applyCommand(it) }
    return state.horizontal * state.depth
}

fun main() {
    val data = File("../inputs/day02.txt").readLines().map { it.toCommand() }
    val answer1 = part1(data)
    println("Answer 1: $answer1") // 2019945
    val answer2 = part2(data)
    println("Answer 2: $answer2") // 1599311480
}