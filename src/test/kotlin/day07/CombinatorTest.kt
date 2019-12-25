package day07

import org.junit.Assert.assertEquals
import org.junit.Test

class CombinatorTest {

    @Test
    fun `return input for length of 1`() {
        assertEquals(
            listOf(listOf(1)),
            Combinator().combinationsWithoutRepetition(listOf(1))
        )
    }

    @Test
    fun `generate combinations for length of 2`() {
        assertEquals(
            listOf(listOf(0, 1), listOf(1, 0)),
            Combinator().combinationsWithoutRepetition(listOf(0, 1))
        )
    }

    @Test
    fun `generate combinations for length of 3`() {
        assertEquals(
            listOf(
                listOf(0, 1, 2),
                listOf(0, 2, 1),
                listOf(1, 0, 2),
                listOf(1, 2, 0),
                listOf(2, 0, 1),
                listOf(2, 1, 0)
            ),
            Combinator().combinationsWithoutRepetition(listOf(0, 1, 2))
        )
    }
}
