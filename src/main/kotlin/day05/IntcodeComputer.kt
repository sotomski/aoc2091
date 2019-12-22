package day05

import java.util.*
import kotlin.math.pow

class IntcodeComputer(initialMemory: List<Int>) {

    private val sharedMemory = mutableListOf(*initialMemory.toTypedArray())
    private var instructionPointer = 0

    private val inputBuffer: Queue<Int> = ArrayDeque()
    private val outputBuffer = mutableListOf<Int>()

    fun output() = outputBuffer.toList()

    fun registerInput(value: Int) {
        inputBuffer.add(value)
    }

    private fun readOpcode() = sharedMemory[instructionPointer] % 100

    private fun readValue(offset: Int): Int {
        val paramModes = sharedMemory[instructionPointer] / 100
        val valueAddress = if (paramModes / 10.0.pow(offset - 1).toInt() % 10 == 0) {
            sharedMemory[instructionPointer + offset]
        } else {
            instructionPointer + offset
        }

        return sharedMemory[valueAddress]
    }

    fun execute(noun: Int? = null, verb: Int? = null): Int {
        noun?.let { sharedMemory[1] = it }
        verb?.let { sharedMemory[2] = it }

        var resultAddress = 0

        loop@ while(true) {
            when(readOpcode()) {
                OP_ADD -> {
                    val firstOperand = readValue(1)
                    val secondOperand = readValue(2)

                    // TODO: Can this case be handled as standard parameter with omitted leading 0?
                    resultAddress = sharedMemory[instructionPointer + 3]
                    instructionPointer += 4

                    sharedMemory[resultAddress] = firstOperand + secondOperand
                }
                OP_MULT -> {
                    val firstOperand = readValue(1)
                    val secondOperand = readValue(2)

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
                    val firstOperand = readValue(1)
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