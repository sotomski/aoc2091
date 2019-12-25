package day07

class Combinator {

    fun combinationsWithoutRepetition(elements: List<Int>): List<List<Int>> {
        return if (elements.count() == 1) {
            listOf(elements)
        } else {
            val combinations = mutableListOf<List<Int>>()

            elements.forEach { el ->
                combinationsWithoutRepetition(elements.minus(el)).forEach { subList ->
                    combinations.add(listOf(el) + subList)
                }
            }

            combinations
        }
    }
}
