package day08

import org.junit.Assert.*
import org.junit.Test

class SpaceImageFormatTest {
    @Test
    fun `checksum when image is trivial`() {
        val image = SpaceImageFormat(intArrayOf(1,0), 1, 1)

        assertEquals(0, image.checksum())
    }

    @Test
    fun `checksum when image is 2 by 2`() {
        val image = SpaceImageFormat(intArrayOf(1, 2, 0, 1, 2, 2, 1, 1), 2, 2)

        assertEquals(4, image.checksum())
    }
    
    @Test
    fun `checksum when image is 3 by 2`() {
        val image = SpaceImageFormat(intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2), 3, 2)

        assertEquals(1, image.checksum())
    }
    
    @Test
    fun `decode should handle one opaque layer`() {
        val image = SpaceImageFormat(intArrayOf(0, 1, 0,  1, 1, 0), 3, 2)
        val expectedDecoded = arrayOf(arrayOf(0, 1, 0), arrayOf(1, 1, 0))

        assertArrayEquals(expectedDecoded, image.decode())
    }

    @Test
    fun `decode should take top opaque pixel when multiple layers present`() {
        val image = SpaceImageFormat(intArrayOf(0, 1, 0, 1, 1, 0, 0, 1, 1,  1, 1, 0), 3, 2)
        val expectedDecoded = arrayOf(arrayOf(0, 1, 0), arrayOf(1, 1, 0))

        assertArrayEquals(expectedDecoded, image.decode())
    }

    @Test
    fun `decode should ignore transparent pixels`() {
        val image = SpaceImageFormat(intArrayOf(0, 1, 2, 2, 1, 0, 0, 1, 1, 0, 1, 0), 3, 2)
        val expectedDecoded = arrayOf(arrayOf(0, 1, 1), arrayOf(0, 1, 0))

        assertArrayEquals(expectedDecoded, image.decode())
    }

    @Test
    fun `decode example`() {
        val image = SpaceImageFormat(intArrayOf(0,2,2,2,1,1,2,2,2,2,1,2,0,0,0,0), 2, 2)
        val expectedDecoded = arrayOf(arrayOf(0, 1), arrayOf(1, 0))

        assertArrayEquals(expectedDecoded, image.decode())
    }
}
