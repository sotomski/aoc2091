package day07

class AmplificationCircuit(private val program: List<Int>) {

    fun findOptimalPhaseSetting(length: Int): List<Int> {
        val basePhaseSettings = (0 until length).toList()
        val allCombinations = Combinator().combinationsWithoutRepetition(basePhaseSettings)
        return allCombinations.maxBy { thrusterSignal(it) } ?: emptyList()
    }

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
}
