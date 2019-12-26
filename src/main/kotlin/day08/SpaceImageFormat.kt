package day08

class SpaceImageFormat(data: IntArray, private val width: Int, private val height: Int) {

    private val pixelCount = width * height
    private val layers = data.toList().windowed(pixelCount, pixelCount)

    fun checksum(): Int {
        //    Find the layer with fewest 0 digits.
        //    On that layer, what is the number of 1 digits multiplied by the number of 2 digits.
        val testLayer = layers.minBy { layer -> layer.count { it == 0 } } ?: emptyList()
        val oneCount = testLayer.count { it == 1 }
        val twoCount = testLayer.count { it == 2 }
        return oneCount * twoCount
    }

    fun decode(): Array<Array<Int>> {
        val pixels = mutableListOf<Int>()
        for(idx in 0 until pixelCount) {
            val opaquePixel = layers.first { it[idx] != TRANSPARENT }[idx]
            pixels.add(opaquePixel)
        }

        return pixels.windowed(width, width).map { it.toTypedArray() }.toTypedArray()
    }

    companion object {
        private const val TRANSPARENT = 2
    }
}
