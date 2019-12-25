package day07

class AmplificationCircuit(private val program: List<Int>) {

    fun findOptimalPhaseSetting(): List<Int> {

        // TODO: Generate all phases' combinations

        // TODO: Calculate the signals for each combination

        // TODO: Return the phases for the largest signal.

        return emptyList()
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
