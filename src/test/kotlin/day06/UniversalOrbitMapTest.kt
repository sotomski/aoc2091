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

    @Test
    fun `shortestRouteBetween should return empty route when system is trivial`() {
        val inputOrbits = listOf("COM)A")

        assertEquals(
            emptyList<Int>(),
            UniversalOrbitMap(inputOrbits).shortestRouteBetween("COM", "A")
        )
    }

    @Test
    fun `shortestRouteBetween should find the route when the orbit system is single-child-only`() {
        val inputOrbits = listOf("COM)A", "A)B", "B)C", "C)D")

        assertEquals(
            listOf("B", "C"),
            UniversalOrbitMap(inputOrbits).shortestRouteBetween("A", "D")
        )
    }

    @Test
    fun `shortestRouteBetween should be empty when endpoints orbit the same object`() {
        val inputOrbits = listOf("COM)A", "A)B", "A)C", "A)D")

        assertEquals(
            emptyList<Int>(),
            UniversalOrbitMap(inputOrbits).shortestRouteBetween("B", "D")
        )
    }

    @Test
    fun `shortestRouteBetween should find a route through the common center of orbiting objects`() {
        val inputOrbits = listOf("COM)B", "B)C", "C)D", "D)E", "E)F", "B)G", "G)H", "D)I", "E)J", "J)K", "K)L", "K)YOU", "I)SAN")

        assertEquals(
            listOf("K", "J", "E", "D", "I"),
            UniversalOrbitMap(inputOrbits).shortestRouteBetween("YOU", "SAN")
        )
    }
}
