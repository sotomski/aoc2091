package day05

class IntcodeComputer {

    fun execute(initialMemory: List<Int>, noun: Int? = null, verb: Int? = null): Int {
        // Initialize memory
        val memory = ArrayList(initialMemory)
        noun?.let { memory[1] = it }
        verb?.let { memory[2] = it }

        var instructionPointer = 0
        fun firstParamAddress() = memory[instructionPointer + 1]
        fun secondParamAddress() = memory[instructionPointer + 2]
        fun resultAddress() = memory[instructionPointer + 3]

        var opcode = 0
        var firstOperand = 0
        var secondOperand = 0
        var result = 0

//    println("===> EXECUTING PROGRAM: $memory")

        loop@ while(opcode != Companion.OP_HALT) {
//        println("Iteration start: $memory")

            // Read values
            opcode = memory[instructionPointer]

            // Perform operation
            result = when(opcode) {
                Companion.OP_ADD -> {
                    firstOperand = memory[firstParamAddress()]
                    secondOperand = memory[secondParamAddress()]
                    firstOperand + secondOperand
                }
                Companion.OP_MULT -> {
                    firstOperand = memory[firstParamAddress()]
                    secondOperand = memory[secondParamAddress()]
                    firstOperand * secondOperand
                }
                Companion.OP_HALT -> break@loop
                else -> throw UnsupportedOperationException()
            }

            memory[resultAddress()] = result

            // Step forward
            instructionPointer += 4

//        println("Iteration end: $memory")

        }

//    println("===> FINISHED EXECUTION")

        return memory[0]
    }

    companion object {
        const val OP_HALT = 99
        const val OP_ADD = 1
        const val OP_MULT = 2
    }
}