package day05

import java.util.*
import kotlin.collections.ArrayList

class IntcodeComputer {

    private val inputBuffer: Queue<Int> = ArrayDeque()
    private val outputBuffer = mutableListOf<Int>()

    fun output() = outputBuffer.toList()

    fun registerInput(value: Int) {
        inputBuffer.add(value)
    }

    fun execute(initialMemory: List<Int>, noun: Int? = null, verb: Int? = null): Int {
        // Initialize memory
        val memory = ArrayList(initialMemory)
        noun?.let { memory[1] = it }
        verb?.let { memory[2] = it }

        var instructionPointer = 0

        var opcode = 0
        var resultAddress = 0

        loop@ while(opcode != OP_HALT) {
            opcode = memory[instructionPointer]

            val result = when(opcode) {
                OP_ADD -> {
                    val firstOperand = memory[memory[instructionPointer + 1]]
                    val secondOperand = memory[memory[instructionPointer + 2]]
                    resultAddress = memory[instructionPointer + 3]
                    instructionPointer += 4

                    firstOperand + secondOperand
                }
                OP_MULT -> {
                    val firstOperand = memory[memory[instructionPointer + 1]]
                    val secondOperand = memory[memory[instructionPointer + 2]]
                    resultAddress = memory[instructionPointer + 3]
                    instructionPointer += 4

                    firstOperand * secondOperand
                }
                OP_INPUT -> {
                    resultAddress = memory[instructionPointer + 1]
                    instructionPointer += 2

                    inputBuffer.poll()
                }
                OP_OUTPUT -> {
                    val firstOperand = memory[memory[instructionPointer + 1]]
                    resultAddress = memory[instructionPointer + 1]
                    instructionPointer += 2

                    outputBuffer.add(firstOperand)

                    firstOperand

                }
                OP_HALT -> break@loop
                else -> throw UnsupportedOperationException()
            }

            memory[resultAddress] = result
        }

        return memory[0]
    }

    companion object {
        const val OP_HALT = 99
        const val OP_ADD = 1
        const val OP_MULT = 2
        const val OP_INPUT = 3
        const val OP_OUTPUT = 4
    }
}