package day09

import day09.IntcodeComputer.ParameterMode.*
import java.util.*
import kotlin.math.pow

class IntcodeComputer(initialMemory: List<Int>) {

    private val sharedMemory = mutableListOf(*initialMemory.toTypedArray())
    private var instructionPointer = 0
    private var relativeBase = 0

    private val inputBuffer: Queue<Int> = ArrayDeque()
    private val outputBuffer = mutableListOf<Int>()

    private enum class ExecutionState { NotRunning, InProgress, WaitingForInput }

    private var state = ExecutionState.NotRunning
    val isExecutionInProgress get() = state != ExecutionState.NotRunning

    fun output() = outputBuffer.toList()

    fun registerInput(value: Int) {
        inputBuffer.add(value)

        if (state == ExecutionState.WaitingForInput) {
            state = ExecutionState.InProgress
            runInstructions()
        }
    }

    private fun readOpcode() = sharedMemory[instructionPointer] % 100

    private fun readValue(offset: Int): Int {
        val address = valueAddress(offset)
        return sharedMemory[address]
    }

    private fun writeValue(offset: Int, value: Int) {
        val address = valueAddress(offset)
        sharedMemory[address] = value
    }

    private fun valueAddress(offset: Int): Int = when (modeOfParameter(offset)) {
        Positional -> sharedMemory[instructionPointer + offset]
        Immediate -> instructionPointer + offset
        Relative -> sharedMemory[relativeBase + offset]
    }

    private fun modeOfParameter(offset: Int): ParameterMode {
        val paramModes = sharedMemory[instructionPointer] / 100
        val intMode = paramModes / 10.0.pow(offset - 1).toInt() % 10
        return ParameterMode.fromOrdinal(intMode)
    }

    fun execute(noun: Int? = null, verb: Int? = null): Int {
        noun?.let { sharedMemory[1] = it }
        verb?.let { sharedMemory[2] = it }

        state = ExecutionState.InProgress

        return runInstructions()
    }

    private fun runInstructions(): Int {
        loop@ while (true) {
            when (readOpcode()) {
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
                    if (inputBuffer.peek() == null) {
                        state = ExecutionState.WaitingForInput
                        break@loop
                    }

                    writeValue(1, inputBuffer.poll())

                    instructionPointer += 2
                }
                OP_OUTPUT -> {
                    val first = readValue(1)
                    outputBuffer.add(first)

                    instructionPointer += 2
                }
                OP_JUMP_IF_TRUE -> {
                    val first = readValue(1)
                    val second = readValue(2)

                    instructionPointer = if (first != 0) {
                        second
                    } else {
                        instructionPointer + 3
                    }
                }
                OP_JUMP_IF_FALSE -> {
                    val first = readValue(1)
                    val second = readValue(2)

                    instructionPointer = if (first == 0) {
                        second
                    } else {
                        instructionPointer + 3
                    }
                }
                OP_LESS_THAN -> {
                    val first = readValue(1)
                    val second = readValue(2)
                    writeValue(3, if (first < second) 1 else 0)

                    instructionPointer += 4
                }
                OP_EQUALS -> {
                    val first = readValue(1)
                    val second = readValue(2)
                    writeValue(3, if (first == second) 1 else 0)

                    instructionPointer += 4
                }
                OP_INCREMENT_RELATIVE_BASE -> {
                    relativeBase += readValue(1)

                    instructionPointer += 2
                }
                OP_HALT -> {
                    state = ExecutionState.NotRunning
                    return sharedMemory[0]
                }
                else -> throw UnsupportedOperationException()
            }
        }

        return Int.MIN_VALUE
    }

    companion object {
        const val OP_HALT = 99
        const val OP_ADD = 1
        const val OP_MULT = 2
        const val OP_INPUT = 3
        const val OP_OUTPUT = 4
        const val OP_JUMP_IF_TRUE = 5
        const val OP_JUMP_IF_FALSE = 6
        const val OP_LESS_THAN = 7
        const val OP_EQUALS = 8
        const val OP_INCREMENT_RELATIVE_BASE = 9

    }

    private enum class Instruction {
        Halt, Add, Multiply, Input, Output, JumpIfTrue, JumpIfFalse, LessThan, Equals, IncrementRelativeBase;

        companion object {
            fun fromOrdinal(ordinal: Int): Instruction {
                return try {
                    values().first { it.ordinal == ordinal}
                } catch (e: Exception) {
                    throw IllegalArgumentException("Unknown value", e)
                }
            }
        }
    }

    private enum class ParameterMode {
        Positional, Immediate, Relative;

        companion object {
            fun fromOrdinal(ordinal: Int): ParameterMode {
                return try {
                    values().first { it.ordinal == ordinal}
                } catch (e: Exception) {
                    throw IllegalArgumentException("Unknown value", e)
                }
            }
        }
    }
}