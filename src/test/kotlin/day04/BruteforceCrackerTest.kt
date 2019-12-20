package day04

import org.junit.Assert.*
import org.junit.Test

class BruteforceCrackerTest {
    @Test
    fun `trivial cases`() {
        val cracker = BruteforceCracker(111111, 999999)

        (111111..999999 step 111111).forEach {
            assertTrue("$it should be a valid password", cracker.isPassword(it))
        }

        assertFalse("223450 is not a valid password", cracker.isPassword(223450))
        assertFalse("123789 is not a valid password", cracker.isPassword(123789))
    }
}