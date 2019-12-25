package day07

class AmplificationCircuit(private val program: List<Int>) {

    fun findOptimalPhaseSetting(withFeedback: Boolean = false): List<Int> {
        val basePhaseSettings = listOf(0, 1, 2, 3, 4)
        val allCombinations = Combinator().combinationsWithoutRepetition(basePhaseSettings)
        return allCombinations.maxBy { thrusterSignal(it) } ?: emptyList()
    }

    // TODO: Once feedback is done, find the underlying behavior abstraction.
    fun thrusterSignal(phases: List<Int>): Int {
        var signal = 0

        for (index in 0 until phases.count()) {
            val computer = IntcodeComputer(ArrayList(program))
            computer.registerInput(phases[index])
            computer.registerInput(signal)
            computer.execute()
            signal = computer.output().first()
        }

        return signal
    }

    // TODO: Isn't INT too small?
    fun thrusterSignalWithFeedback(phases: List<Int>): Int {
        var signal = 0

        val computers = (0 until phases.count()).map { phaseIdx ->
            IntcodeComputer(ArrayList(program)).apply {
                registerInput(phases[phaseIdx])
            }
        }

//        // TODO: While the last computer is still running...
//        while(true) {
            for(computer in computers) {
                computer.registerInput(signal) // TODO: Input should automatically resume a paused program.
                computer.execute() // TODO: Only if it's not running?
                signal = computer.output().last() // TODO: By now, the computer should again be paused.
            }
//        }

        return signal
    }
}
