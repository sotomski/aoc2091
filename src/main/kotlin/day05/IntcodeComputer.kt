package day05

import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow

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
            opcode = memory[instructionPointer] % 100
            val paramModes = memory[instructionPointer] / 100

            when(opcode) {
                OP_ADD -> {
                    val firstOperandPosition = if (paramModes / 10.0.pow(1-1).toInt() == 0) {
                        memory[instructionPointer + 1]
                    } else {
                        instructionPointer + 1
                    }
                    val firstOperand = memory[firstOperandPosition]

                    val secondOperandPosition = if (paramModes / 10.0.pow(2-1).toInt() == 0) {
                        memory[instructionPointer + 2]
                    } else {
                        instructionPointer + 2
                    }
                    val secondOperand = memory[secondOperandPosition]

                    resultAddress = memory[instructionPointer + 3]
                    instructionPointer += 4

                    memory[resultAddress] = firstOperand + secondOperand
                }
                OP_MULT -> {
                    val firstOperandPosition = if (paramModes / 10.0.pow(1-1).toInt() == 0) {
                        memory[instructionPointer + 1]
                    } else {
                        instructionPointer + 1
                    }
                    val firstOperand = memory[firstOperandPosition]

                    val secondOperandPosition = if (paramModes / 10.0.pow(2-1).toInt() == 0) {
                        memory[instructionPointer + 2]
                    } else {
                        instructionPointer + 2
                    }
                    val secondOperand = memory[secondOperandPosition]

                    resultAddress = memory[instructionPointer + 3]
                    instructionPointer += 4

                    memory[resultAddress] = firstOperand * secondOperand
                }
                OP_INPUT -> {
                    resultAddress = if (paramModes / 10.0.pow(1-1).toInt() == 0) {
                        memory[instructionPointer + 1]
                    } else {
                        instructionPointer + 1
                    }

                    instructionPointer += 2

                    memory[resultAddress] = inputBuffer.poll()
                }
                OP_OUTPUT -> {
                    val firstOperandPosition = if (paramModes / 10.0.pow(1-1).toInt() == 0) {
                        memory[instructionPointer + 1]
                    } else {
                        instructionPointer + 1
                    }
                    val firstOperand = memory[firstOperandPosition]
                    instructionPointer += 2

                    outputBuffer.add(firstOperand)
                }
                OP_HALT -> break@loop
                else -> throw UnsupportedOperationException()
            }
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