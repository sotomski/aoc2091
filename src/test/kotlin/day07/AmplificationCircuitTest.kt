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

    @Test
    fun `find optimal phase setting combination`() {
        assertEquals(
            listOf(4, 3, 2, 1, 0),
            AmplificationCircuit(listOf(3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0)).findOptimalPhaseSetting()
        )

        assertEquals(
            listOf(0, 1, 2,  3, 4),
            AmplificationCircuit(listOf(3,23,3,24,1002,24,10,24,1002,23,-1,23, 101,5,23,23,1,24,23,23,4,23,99,0,0)).findOptimalPhaseSetting()
        )

        assertEquals(
            listOf(1, 0, 4, 3, 2),
            AmplificationCircuit(listOf(3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33, 1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0)).findOptimalPhaseSetting()
        )
    }

    // TODO: Test drive the feedback


    // TODO: Put the examples here as well to prove the overall correctness of the algorithm.
}
