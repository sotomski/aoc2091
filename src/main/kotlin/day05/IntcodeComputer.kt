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

    private fun writeValue(offset: Int, value: Int) {
        val paramModes = sharedMemory[instructionPointer] / 100
        val valueAddress = if (paramModes / 10.0.pow(offset - 1).toInt() % 10 == 0) {
            sharedMemory[instructionPointer + offset]
        } else {
            instructionPointer + offset
        }

        sharedMemory[valueAddress] = value
    }

    fun execute(noun: Int? = null, verb: Int? = null): Int {
        noun?.let { sharedMemory[1] = it }
        verb?.let { sharedMemory[2] = it }

        loop@ while(true) {
            when(readOpcode()) {
                OP_ADD -> {
                    val first = readValue(1)
                    val second = readValue(2)
                    writeValue(3, first + second)

                    instructionPointer += 4
                }
                OP_MULT -> {
                    val first = readValue(1)
                    val second = readValue(2)
                    writeValue(3, first * second)

                    instructionPointer += 4
                }
                OP_INPUT -> {
                    writeValue(1, inputBuffer.poll())

                    instructionPointer += 2
                }
                OP_OUTPUT -> {
                    val first = readValue(1)
                    outputBuffer.add(first)

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
        const val OP_JUMP_IF_TRUE = 5
    }
}