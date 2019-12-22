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
    fun `INPUT operation`() {
        val computer = IntcodeComputer()

        computer.registerInput(42)
        val got = computer.execute(listOf(INPUT, 0, 99))

        assertEquals(    42, got)
    }

    @Test
    fun `OUTPUT operation`() {
        val computer = IntcodeComputer()

        computer.registerInput(42)
        computer.execute(listOf(INPUT, 0, OUTPUT, 0, 99))

        assertEquals(42, computer.output().last())
    }

    // TODO: immediate mode
}