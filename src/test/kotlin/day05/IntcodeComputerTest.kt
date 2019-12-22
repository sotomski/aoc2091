package day05

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.pow

// Following is meant to improve test readability
private const val ADD = 1
private const val MULT = 2
private const val INPUT = 3
private const val OUTPUT = 4

class IntcodeComputerTest {

    @Test
    fun `ADD in default positional mode`() {
        assertEquals(
            3,
            IntcodeComputer().execute(listOf(ADD, 2, 1, 0, 99))
        )
        assertEquals(
            3 * 99,
            IntcodeComputer().execute(listOf(ADD, 8, 8, 0, ADD, 0, 8, 0, 99))
        )
    }

    @Test
    fun `ADD in immediate mode`() {
        val opcode = ADD + 100 + 1000

        assertEquals(
            8,
            IntcodeComputer().execute(listOf(opcode, 5, 3, 0, 99))
        )
    }

    @Test
    fun `MULT in default positional mode`() {
        assertEquals(
            2,
            IntcodeComputer().execute(listOf(MULT, 2, 1, 0, 99))
        )
        assertEquals(
            99.0.pow(3.0).toInt(),
            IntcodeComputer().execute(listOf(MULT, 8, 8, 0, MULT, 0, 8, 0, 99))
        )
    }

    @Test
    fun `MULT in immediate mode`() {
        val opcode = MULT + 100 + 1000

        assertEquals(
            20,
            IntcodeComputer().execute(listOf(opcode, 5, 4, 0, 99))
        )
    }

    @Test
    fun `immediate mode should set omitted params modes to zero`() {
        val opcode = ADD + 100 // second param in positional mode

        assertEquals(
            90,
            IntcodeComputer().execute(listOf(opcode, -11, 0, 0, 99))
        )
    }

    @Test
    fun `INPUT operation`() {
        val computer = IntcodeComputer()

        computer.registerInput(42)
        val got = computer.execute(listOf(INPUT, 0, 99))

        assertEquals(    42, got)
    }

    @Test
    fun `INPUT operation in immediate mode`() {
        val computer = IntcodeComputer()

        computer.registerInput(42)
        computer.execute(listOf(INPUT + 100, 0, OUTPUT, 1, 99))
        val got = computer.output().last()

        assertEquals(    42, got)
    }

    @Test
    fun `OUTPUT operation`() {
        val computer = IntcodeComputer()

        computer.registerInput(42)
        computer.execute(listOf(INPUT, 0, OUTPUT, 0, 99))

        assertEquals(42, computer.output().last())
    }

    @Test
    fun `OUTPUT operation in immediate mode`() {
        val computer = IntcodeComputer().apply {
            execute(listOf(OUTPUT + 100, -57, 99))
        }

        assertEquals(-57, computer.output().last())
    }
}