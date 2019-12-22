package day05

import java.util.*
import kotlin.math.pow

class IntcodeComputer(initialMemory: List<Int>) {

    private val sharedMemory = mutableListOf(*initialMemory.toTypedArray())
    private val inputBuffer: Queue<Int> = ArrayDeque()
    private val outputBuffer = mutableListOf<Int>()

    fun output() = outputBuffer.toList()

    fun registerInput(value: Int) {
        inputBuffer.add(value)
    }

    fun execute(noun: Int? = null, verb: Int? = null): Int {
        // Initialize memory
        noun?.let { sharedMemory[1] = it }
        verb?.let { sharedMemory[2] = it }

        var instructionPointer = 0

        var opcode = 0
        var resultAddress = 0

        loop@ while(opcode != OP_HALT) {
            opcode = sharedMemory[instructionPointer] % 100
            val paramModes = sharedMemory[instructionPointer] / 100

            when(opcode) {
                OP_ADD -> {
                    val firstOperandPosition = if (paramModes / 10.0.pow(1-1).toInt() % 10 == 0) {
                        sharedMemory[instructionPointer + 1]
                    } else {
                        instructionPointer + 1
                    }
                    val firstOperand = sharedMemory[firstOperandPosition]

                    val secondOperandPosition = if (paramModes / 10.0.pow(2-1).toInt() % 10 == 0) {
                        sharedMemory[instructionPointer + 2]
                    } else {
                        instructionPointer + 2
                    }
                    val secondOperand = sharedMemory[secondOperandPosition]

                    resultAddress = sharedMemory[instructionPointer + 3]
                    instructionPointer += 4

                    sharedMemory[resultAddress] = firstOperand + secondOperand
                }
                OP_MULT -> {
                    val firstOperandPosition = if (paramModes / 10.0.pow(1-1).toInt() % 10 == 0) {
                        sharedMemory[instructionPointer + 1]
                    } else {
                        instructionPointer + 1
                    }
                    val firstOperand = sharedMemory[firstOperandPosition]

                    val secondOperandPosition = if (paramModes / 10.0.pow(2-1).toInt() % 10 == 0) {
                        sharedMemory[instructionPointer + 2]
                    } else {
                        instructionPointer + 2
                    }
                    val secondOperand = sharedMemory[secondOperandPosition]

                    resultAddress = sharedMemory[instructionPointer + 3]
                    instructionPointer += 4

                    sharedMemory[resultAddress] = firstOperand * secondOperand
                }
                OP_INPUT -> {
                    resultAddress = if (paramModes / 10.0.pow(1-1).toInt() % 10 == 0) {
                        sharedMemory[instructionPointer + 1]
                    } else {
                        instructionPointer + 1
                    }

                    instructionPointer += 2

                    sharedMemory[resultAddress] = inputBuffer.poll()
                }
                OP_OUTPUT -> {
                    val firstOperandPosition = if (paramModes / 10.0.pow(1-1).toInt() % 10 == 0) {
                        sharedMemory[instructionPointer + 1]
                    } else {
                        instructionPointer + 1
                    }
                    val firstOperand = sharedMemory[firstOperandPosition]
                    instructionPointer += 2

                    outputBuffer.add(firstOperand)
                }
                OP_HALT -> break@loop
                else -> throw UnsupportedOperationException()
            }
        }

        return sharedMemory[0]
    }

    companion object {
        const val OP_HALT = 99
        const val OP_ADD = 1
        const val OP_MULT = 2
        const val OP_INPUT = 3
        const val OP_OUTPUT = 4
    }
}