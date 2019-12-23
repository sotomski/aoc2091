package day06

class UniversalOrbitMap(
    inputOrbits: List<String>
) {
    private val map: MapNode = createNode("COM", inputOrbits)

    private fun createNode(name: String, inputOrbits: List<String>): MapNode {
        // TODO: Would it help to somehow sort the inputOrbits before building the tree?
        val findOrbitsWithCenter = { center: String ->
            inputOrbits.map { it.split(')') }.filter { it.first() == center }
        }

        return MapNode(
            name = name,
            children = findOrbitsWithCenter(name).map {
                createNode(it.last(), inputOrbits)
            }
        )
    }

    fun checksum() = sumNodeDepths(map, 0)

    private fun sumNodeDepths(node: MapNode, level: Int): Int {
        return level + node.children.sumBy { sumNodeDepths(it, level + 1) }
    }
}

data class MapNode(
    val name: String,
    val children: List<MapNode>
)
