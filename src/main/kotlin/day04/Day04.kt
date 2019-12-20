package day04

fun main() {
    part1("171309-643603")
}

fun part1(rawInput: String) {
    val input = rawInput.split('-').map { it.toInt() }

    val cracker = BruteforceCracker(input[0], input[1])
    val count = cracker.passwordCount()

    println("Count of possible passwords for $input: $count")
}

