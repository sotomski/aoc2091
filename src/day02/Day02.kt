package day02

fun main() {

    println("Starting part 1")
    println("Result of part 1: ${part1()}")

    println("Starting part 2")
    val initialMemory = listOf(1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,1,6,19,1,9,19,23,1,6,23,27,1,10,27,31,1,5,31,35,2,6,35,39,1,5,39,43,1,5,43,47,2,47,6,51,1,51,5,55,1,13,55,59,2,9,59,63,1,5,63,67,2,67,9,71,1,5,71,75,2,10,75,79,1,6,79,83,1,13,83,87,1,10,87,91,1,91,5,95,2,95,10,99,2,9,99,103,1,103,6,107,1,107,10,111,2,111,10,115,1,115,6,119,2,119,9,123,1,123,6,127,2,127,10,131,1,131,6,135,2,6,135,139,1,139,5,143,1,9,143,147,1,13,147,151,1,2,151,155,1,10,155,0,99,2,14,0,0)
    val foundParams = findNounAndVerb(initialMemory, 19690720)
    println("Result of part 2: $foundParams")
    println("Sanity check: ${19690720 == executeIntcode(initialMemory, foundParams.first, foundParams.second)}")
}

fun part1(): Int {
    val initialMemory = arrayListOf(1,0,0,3,1,1,2,3,1,3,4,3,1,5,0,3,2,1,6,19,1,9,19,23,1,6,23,27,1,10,27,31,1,5,31,35,2,6,35,39,1,5,39,43,1,5,43,47,2,47,6,51,1,51,5,55,1,13,55,59,2,9,59,63,1,5,63,67,2,67,9,71,1,5,71,75,2,10,75,79,1,6,79,83,1,13,83,87,1,10,87,91,1,91,5,95,2,95,10,99,2,9,99,103,1,103,6,107,1,107,10,111,2,111,10,115,1,115,6,119,2,119,9,123,1,123,6,127,2,127,10,131,1,131,6,135,2,6,135,139,1,139,5,143,1,9,143,147,1,13,147,151,1,2,151,155,1,10,155,0,99,2,14,0,0)
    return executeIntcode(initialMemory, 12, 2)
}

fun findNounAndVerb(initialMemory: List<Int>, output: Int): Pair<Int, Int> {
    // Since both noun and verb represent addresses in memory (which is small), a brute force solution should suffice.
    val maxAddress = initialMemory.size - 1

    for (noun in 0..maxAddress)
        for (verb in 0..maxAddress) {
            val result = executeIntcode(initialMemory, noun, verb)
            if (result == output)
                return Pair(noun, verb)
        }

    return Pair(Int.MIN_VALUE, Int.MIN_VALUE)
}

const val OP_HALT = 99
const val OP_ADD = 1
const val OP_MULT = 2

fun executeIntcode(initialMemory: List<Int>, noun: Int, verb: Int): Int {
    // Initialize memory
    val memory = ArrayList(initialMemory)
    memory[1] = noun
    memory[2] = verb

    var instructionPointer = 0
    fun firstParamAddress() = memory[instructionPointer + 1]
    fun secondParamAddress() = memory[instructionPointer + 2]
    fun resultAddress() = memory[instructionPointer + 3]

    var opcode = 0
    var firstOperand = 0
    var secondOperand = 0
    var result = 0

//    println("===> EXECUTING PROGRAM: $memory")

    loop@ while(opcode != OP_HALT) {
//        println("Iteration start: $memory")

        // Read values
        opcode = memory[instructionPointer]

        // Perform operation
        result = when(opcode) {
            OP_ADD -> {
                firstOperand = memory[firstParamAddress()]
                secondOperand = memory[secondParamAddress()]
                firstOperand + secondOperand
            }
            OP_MULT -> {
                firstOperand = memory[firstParamAddress()]
                secondOperand = memory[secondParamAddress()]
                firstOperand * secondOperand
            }
            OP_HALT -> break@loop
            else -> throw UnsupportedOperationException()
        }

        memory[resultAddress()] = result

        // Step forward
        instructionPointer += 4

//        println("Iteration end: $memory")

    }

//    println("===> FINISHED EXECUTION")

    return memory[0]
}