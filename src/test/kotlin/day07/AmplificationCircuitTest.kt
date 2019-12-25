package day07

import ADD
import INPUT
import OUTPUT
import org.junit.Assert.assertEquals
import org.junit.Test

class AmplificationCircuitTest {
    @Test
    fun `thrusterSignal should use phase setting as first input`() {
        val circuit = AmplificationCircuit(
            listOf(INPUT + 100, 1, INPUT + 100, 3, OUTPUT, 1, 99)
        )

        assertEquals(42, circuit.thrusterSignal(listOf(42)))
    }

    @Test
    fun `thrusterSignal should initialize calculation with signal equal to 0`() {
        val circuit = AmplificationCircuit(
            listOf(INPUT + 100, 1, INPUT + 100, 3, OUTPUT, 3, 99)
        )

        assertEquals(0, circuit.thrusterSignal(listOf(42)))
    }
    
    @Test
    fun `thrusterSignal should use previous amplifier output as input for the next`() {
        val programToSumPhases = listOf(INPUT + 100, 1, INPUT + 100, 3, ADD, 1, 3, 5, OUTPUT, 5, 99)
        val circuit = AmplificationCircuit(programToSumPhases)

        assertEquals(18, circuit.thrusterSignal(listOf(3, 4, 5, 6)))
    }
}
