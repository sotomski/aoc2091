package day03

import org.junit.Assert.*
import org.junit.Test

class WireTest {
    @Test
    fun `stepsTo should count steps until first turn`() {
        assertEquals(5,
            Wire.parse("R10").stepsTo(Point(5, 0))
        )

        assertEquals(7,
            Wire.parse("L7").stepsTo(Point(-7, 0))
        )

        assertEquals(15,
            Wire.parse("U15").stepsTo(Point(0, 15))
        )

        assertEquals(35,
            Wire.parse("D100").stepsTo(Point(0, -35))
        )
    }

    @Test
    fun `stepsTo should handle turns`() {
        assertEquals(19,
            Wire.parse("R10,U7,L5").stepsTo(Point(8, 7))
        )
    }
}
