package day06

import org.junit.Assert.assertEquals
import org.junit.Test

class UniversalOrbitMapTest {

    @Test
    fun `checksum should be 0 when map is empty`() {
        assertEquals(0, UniversalOrbitMap(emptyList()).checksum())
    }

    @Test
    fun `checksum should be 1 when map has only one orbiting object`() {
        assertEquals(
            1,
            UniversalOrbitMap(listOf("COM)A")).checksum()
        )
    }

    @Test
    fun `checksum should sum sibling nodes`() {
        val inputOrbits = listOf("COM)A", "COM)B", "COM)B")

        assertEquals(
            3,
            UniversalOrbitMap(inputOrbits).checksum()
        )
    }

    @Test
    fun `checksum should sum children map nodes`() {
        val inputOrbits = listOf("COM)A", "A)B", "B)C")

        assertEquals(
            6,
            UniversalOrbitMap(inputOrbits).checksum()
        )
    }

    @Test
    fun `checksum should sum depths of all nodes`() {
        val inputOrbits = listOf("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L")

        assertEquals(
            42,
            UniversalOrbitMap(inputOrbits).checksum()
        )
    }
}
