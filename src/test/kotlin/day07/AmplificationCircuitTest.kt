package day07

import ADD
import HALT
import INPUT
import OUTPUT
import org.junit.Assert.assertEquals
import org.junit.Test

class AmplificationCircuitTest {
    @Test
    fun `thrusterSignal should use phase setting as first input`() {
        val circuit = AmplificationCircuit(
            listOf(INPUT + 100, 1, INPUT + 100, 3, OUTPUT, 1, HALT)
        )

        assertEquals(42, circuit.thrusterSignal(listOf(42)))
    }

    @Test
    fun `thrusterSignal should initialize calculation with signal equal to 0`() {
        val circuit = AmplificationCircuit(
            listOf(INPUT + 100, 1, INPUT + 100, 3, OUTPUT, 3, HALT)
        )

        assertEquals(0, circuit.thrusterSignal(listOf(42)))
    }

    @Test
    fun `thrusterSignal should use previous amplifier output as input for the next`() {
        val programToSumPhases = listOf(INPUT + 100, 1, INPUT + 100, 3, ADD, 1, 3, 5, OUTPUT, 5, HALT)
        val circuit = AmplificationCircuit(programToSumPhases)

        assertEquals(18, circuit.thrusterSignal(listOf(3, 4, 5, 6)))
    }

    @Test
    fun `thrusterSignal should use previous amplifier output as input for the next when program has multiple inputs`() {
        val programToSumPhases = listOf(
            INPUT + 100, 1,
            INPUT + 100, 3,
            ADD, 1, 3, 5,
            OUTPUT, 5,
            INPUT + 100, 11,
            ADD, 5, 11, 13,
            OUTPUT, 13,
            HALT)
        val circuit = AmplificationCircuit(programToSumPhases)

        // 16 not 12 because the computer overwrites its initial memory during first iteration of the loop.
        // I cannot be bothered to write a better input program, sorry.
        assertEquals(16, circuit.thrusterSignal(listOf(1, 2, 3)))
    }

    @Test
    fun `find optimal phase setting combination`() {
        assertEquals(
            listOf(4, 3, 2, 1, 0),
            AmplificationCircuit(listOf(3,15,3,16,1002,16,10,16,1,16,15,15,4,15,HALT,0,0)).findOptimalPhaseSetting()
        )

        assertEquals(
            listOf(0, 1, 2,  3, 4),
            AmplificationCircuit(listOf(3,23,3,24,1002,24,10,24,1002,23,-1,23, 101,5,23,23,1,24,23,23,4,23,HALT,0,0)).findOptimalPhaseSetting()
        )

        assertEquals(
            listOf(1, 0, 4, 3, 2),
            AmplificationCircuit(listOf(3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33, 1002,33,7,33,1,33,31,31,1,32,31,31,4,31,HALT,0,0,0)).findOptimalPhaseSetting()
        )
    }

    @Test
    fun `find optimal phase setting combination with feedback`() {
        assertEquals(
            listOf(9, 8, 7, 6, 5),
            AmplificationCircuit(listOf(3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26, 27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5)).findOptimalPhaseSettingWithFeedback()
        )

        assertEquals(
            listOf(9, 7, 8, 5, 6),
            AmplificationCircuit(listOf(3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54, -5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4, 53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10)).findOptimalPhaseSettingWithFeedback()
        )
    }
}
