package day07

class AmplificationCircuit(private val program: List<Int>) {

    fun findOptimalPhaseSetting() = findOptimalPhaseFor(listOf(0, 1, 2, 3, 4))

    fun findOptimalPhaseSettingWithFeedback() = findOptimalPhaseFor(listOf(5, 6, 7, 8, 9))

    private fun findOptimalPhaseFor(basePhases: List<Int>): List<Int> {
        val allCombinations = Combinator().combinationsWithoutRepetition(basePhases)
        return allCombinations.maxBy { thrusterSignal(it) } ?: emptyList()
    }

    fun thrusterSignal(phases: List<Int>): Int {
        var signal = 0

        val computers = (0 until phases.count()).map { phaseIdx ->
            IntcodeComputer(ArrayList(program)).apply {
                registerInput(phases[phaseIdx])
            }
        }

        do {
            for(computer in computers) {
                computer.registerInput(signal)
                if (!computer.isExecutionInProgress) {
                    computer.execute()
                }
                signal = computer.output().last()
            }
        } while(computers.last().isExecutionInProgress)

        return signal
    }
}
