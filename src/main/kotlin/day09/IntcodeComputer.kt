package day09

import day09.IntcodeComputer.Instruction.*
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

    private fun readOpcode(): Instruction {
        val intCode = sharedMemory[instructionPointer] % 100
        return Instruction.fromCode(intCode)
    }

    private fun readValue(offset: Int): Int {
        val address = valueAddress(offset)
        return sharedMemory[address]
    }

    private fun writeValue(offset: Int, value: Int) {
        val address = valueAddress(offset)
        sharedMemory[address] = value
    }

    private fun valueAddress(offset: Int): Int = when (modeOfParameter(offset)) {
        POSITIONAL -> sharedMemory[instructionPointer + offset]
        IMMEDIATE -> instructionPointer + offset
        RELATIVE -> relativeBase + sharedMemory[instructionPointer + offset]
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
                ADD -> {
                    val first = readValue(1)
                    val second = readValue(2)
                    writeValue(3, first + second)

                    instructionPointer += 4
                }
                MULTIPLY -> {
                    val first = readValue(1)
                    val second = readValue(2)
                    writeValue(3, first * second)

                    instructionPointer += 4
                }
                INPUT -> {
                    if (inputBuffer.peek() == null) {
                        state = ExecutionState.WaitingForInput
                        break@loop
                    }

                    writeValue(1, inputBuffer.poll())

                    instructionPointer += 2
                }
                OUTPUT -> {
                    val first = readValue(1)
                    outputBuffer.add(first)

                    instructionPointer += 2
                }
                JUMP_IF_TRUE -> {
                    val first = readValue(1)
                    val second = readValue(2)

                    instructionPointer = if (first != 0) {
                        second
                    } else {
                        instructionPointer + 3
                    }
                }
                JUMP_IF_FALSE -> {
                    val first = readValue(1)
                    val second = readValue(2)

                    instructionPointer = if (first == 0) {
                        second
                    } else {
                        instructionPointer + 3
                    }
                }
                LESS_THAN -> {
                    val first = readValue(1)
                    val second = readValue(2)
                    writeValue(3, if (first < second) 1 else 0)

                    instructionPointer += 4
                }
                EQUALS -> {
                    val first = readValue(1)
                    val second = readValue(2)
                    writeValue(3, if (first == second) 1 else 0)

                    instructionPointer += 4
                }
                INCREMENT_RELATIVE_BASE -> {
                    relativeBase += readValue(1)

                    instructionPointer += 2
                }
                HALT -> {
                    state = ExecutionState.NotRunning
                    return sharedMemory[0]
                }
            }
        }

        return Int.MIN_VALUE
    }

    private enum class Instruction(val code: Int) {
        HALT(99),
        ADD(1),
        MULTIPLY(2),
        INPUT(3),
        OUTPUT(4),
        JUMP_IF_TRUE(5),
        JUMP_IF_FALSE(6),
        LESS_THAN(7),
        EQUALS(8),
        INCREMENT_RELATIVE_BASE(9);

        companion object {
            fun fromCode(code: Int): Instruction {
                return try {
                    values().first { it.code == code }
                } catch (e: Exception) {
                    throw IllegalArgumentException("Unknown value", e)
                }
            }
        }
    }

    private enum class ParameterMode {
        POSITIONAL, IMMEDIATE, RELATIVE;

        companion object {
            fun fromOrdinal(ordinal: Int): ParameterMode {
                return try {
                    values().first { it.ordinal == ordinal }
                } catch (e: Exception) {
                    throw IllegalArgumentException("Unknown value", e)
                }
            }
        }
    }
}