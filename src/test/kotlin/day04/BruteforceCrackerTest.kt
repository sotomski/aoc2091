package day04

import org.junit.Assert.*
import org.junit.Test

class BruteforceCrackerTest {
    @Test
    fun `should handle part 1`() {
        val cracker = BruteforceCracker(111111, 999999)

        (111111..999999 step 111111).forEach {
            assertTrue("$it should be a valid password", cracker.isPassword(it))
        }

        assertFalse("223450 is not a valid password", cracker.isPassword(223450))
        assertFalse("123789 is not a valid password", cracker.isPassword(123789))
    }

    @Test
    fun `should handle part 2`() {
        val cracker = BruteforceCracker(111111, 999999)

        assertTrue("$112233 should be a valid password", cracker.isPasswordWithStrictPairs(112233))
        assertTrue("111122 should be a valid password", cracker.isPasswordWithStrictPairs(111122))
        assertFalse("123444 is not a valid password", cracker.isPasswordWithStrictPairs(123444))
    }
}
