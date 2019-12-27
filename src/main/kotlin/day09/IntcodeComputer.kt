package day09

import day09.IntcodeComputer.Instruction.*
import day09.IntcodeComputer.ParameterMode.*
import java.util.*
import kotlin.math.pow

class IntcodeComputer(initialMemory: List<Long>) {

    private val sharedMemory = ExpandableMemory(initialMemory)
    private var instructionPointer = 0L
    private var relativeBase = 0L

    private val inputBuffer: Queue<Long> = ArrayDeque()
    private val outputBuffer = mutableListOf<Long>()

    // TODO: Move execution state definition to the end of the file and rename its values.
    private enum class ExecutionState { NotRunning, InProgress, WaitingForInput }

    private var state = ExecutionState.NotRunning
    val isExecutionInProgress get() = state != ExecutionState.NotRunning

    fun output() = outputBuffer.toList()

    fun registerInput(value: Long) {
        inputBuffer.add(value)

        if (state == ExecutionState.WaitingForInput) {
            state = ExecutionState.InProgress
            runInstructions()
        }
    }

    private fun readOpcode(): Instruction {
        val intCode = sharedMemory[instructionPointer] % 100
        return Instruction.fromCode(intCode.toInt())
    }

    private fun readValue(offset: Long): Long {
        val address = valueAddress(offset)
        return sharedMemory[address]
    }

    private fun writeValue(offset: Long, value: Long) {
        val address = valueAddress(offset)
        sharedMemory[address] = value
    }

    private fun valueAddress(offset: Long): Long = when (modeOfParameter(offset)) {
        POSITIONAL -> sharedMemory[instructionPointer + offset]
        IMMEDIATE -> instructionPointer + offset
        RELATIVE -> relativeBase + sharedMemory[instructionPointer + offset]
    }

    private fun modeOfParameter(offset: Long): ParameterMode {
        val paramModes = sharedMemory[instructionPointer] / 100
        val intMode = (paramModes / 10.0.pow(offset.toInt() - 1) % 10).toInt()
        return ParameterMode.fromOrdinal(intMode)
    }

    fun execute(noun: Long? = null, verb: Long? = null): Long {
        noun?.let { sharedMemory[1] = it }
        verb?.let { sharedMemory[2] = it }

        state = ExecutionState.InProgress

        return runInstructions()
    }

    private fun runInstructions(): Long {
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

                    instructionPointer = if (first != 0L) {
                        second
                    } else {
                        instructionPointer + 3
                    }
                }
                JUMP_IF_FALSE -> {
                    val first = readValue(1)
                    val second = readValue(2)

                    instructionPointer = if (first == 0L) {
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

        return Long.MIN_VALUE
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

    private class ExpandableMemory(initialMemory: List<Long>) {
        private val memory = mutableListOf(*initialMemory.toTypedArray())

        operator fun get(index: Long): Long {
            ensureMemoryLargeEnough(index.toInt())

            return memory[index.toInt()]
        }

        operator fun set(index: Long, value: Long) {
            ensureMemoryLargeEnough(index.toInt())

            memory[index.toInt()] = value
        }

        private fun ensureMemoryLargeEnough(index: Int) {
            if (index >= memory.count()) {
                memory += MutableList(index - memory.count() + 1) { 0L }
            }
        }
    }
}