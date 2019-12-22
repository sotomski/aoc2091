package day05

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.pow

// Following is meant to improve test readability
private const val ADD = 1
private const val MULT = 2
private const val INPUT = 3
private const val OUTPUT = 4
private const val JUMP_IF_TRUE = 5
private const val JUMP_IF_FALSE = 6

class IntcodeComputerTest {

    @Test
    fun `ADD in default positional mode`() {
        assertEquals(
            3,
            IntcodeComputer(listOf(ADD, 2, 1, 0, 99)).execute()
        )
        assertEquals(
            3 * 99,
            IntcodeComputer(listOf(ADD, 8, 8, 0, ADD, 0, 8, 0, 99)).execute()
        )
    }

    @Test
    fun `ADD in immediate mode`() {
        val opcode = ADD + 1000

        assertEquals(
            1008,
            IntcodeComputer(listOf(opcode, 0, 7, 0, 99)).execute()
        )
    }

    @Test
    fun `MULT in default positional mode`() {
        assertEquals(
            2,
            IntcodeComputer(listOf(MULT, 2, 1, 0, 99)).execute()
        )
        assertEquals(
            99.0.pow(3.0).toInt(),
            IntcodeComputer(listOf(MULT, 8, 8, 0, MULT, 0, 8, 0, 99)).execute()
        )
    }

    @Test
    fun `MULT in immediate mode`() {
        val opcode = MULT + 100 + 1000

        assertEquals(
            20,
            IntcodeComputer(listOf(opcode, 5, 4, 0, 99)).execute()
        )
    }

    @Test
    fun `immediate mode should set omitted params modes to zero`() {
        val opcode = ADD + 100 // second param in positional mode

        assertEquals(
            90,
            IntcodeComputer(listOf(opcode, -11, 0, 0, 99)).execute()
        )
    }

    @Test
    fun `INPUT operation`() {
        val computer = IntcodeComputer(listOf(INPUT, 0, 99))

        computer.registerInput(42)
        val got = computer.execute()

        assertEquals(    42, got)
    }

    @Test
    fun `INPUT operation in immediate mode`() {
        val computer = IntcodeComputer(listOf(INPUT + 100, 0, OUTPUT, 1, 99))

        computer.registerInput(42)
        computer.execute()
        val got = computer.output().last()

        assertEquals(    42, got)
    }

    @Test
    fun `OUTPUT operation`() {
        val computer = IntcodeComputer(listOf(INPUT, 0, OUTPUT, 0, 99))

        computer.registerInput(42)
        computer.execute()

        assertEquals(42, computer.output().last())
    }

    @Test
    fun `OUTPUT operation in immediate mode`() {
        val computer = IntcodeComputer(listOf(OUTPUT + 100, -57, 99)).apply {
            execute()
        }

        assertEquals(-57, computer.output().last())
    }

    @Test
    fun `JUMP-IF-TRUE should jump if when param is non-zero`() {
        val opcode = JUMP_IF_TRUE + 1000
        val computer = IntcodeComputer(listOf(opcode, 1, 4, 44444, 99))

        assertEquals(opcode, computer.execute())
    }

    @Test
    fun `JUMP-IF-TRUE should do nothing when first param is zero`() {
        val opcode = JUMP_IF_TRUE + 1100
        val computer = IntcodeComputer(listOf(opcode, 0, 44444, 99))

        assertEquals(opcode, computer.execute())
    }

    @Test
    fun `JUMP-IF-FALSE should jump if when param is zero`() {
        val opcode = JUMP_IF_FALSE + 1100
        val computer = IntcodeComputer(listOf(opcode, 0, 4, 44444, 99))

        assertEquals(opcode, computer.execute())
    }

    @Test
    fun `JUMP-IF-FALSE should do nothing when first param is non-zero`() {
        val opcode = JUMP_IF_FALSE + 1000
        val computer = IntcodeComputer(listOf(opcode, 1, 44444, 99))

        assertEquals(opcode, computer.execute())
    }
}