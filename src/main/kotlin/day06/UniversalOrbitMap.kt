package day06

import java.lang.Integer.min

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

    fun shortestRouteBetween(start: String, end: String): List<String> {
        val pathToStart = mutableListOf<MapNode>()
        findRouteFromRootTo(start, map, pathToStart)

        val pathToEnd = mutableListOf<MapNode>()
        findRouteFromRootTo(end, map, pathToEnd)

        val ancestor = findLowestCommonAncestor(pathToStart, pathToEnd)

        val fromStartToAncestor = pathToStart.reversed().takeWhile { it != ancestor }
        val fromAncestorToEnd = pathToEnd.dropWhile { it != ancestor }

        // Drop start and end after combining the routes.
        val route = fromStartToAncestor.plus(fromAncestorToEnd).drop(1).dropLast(1)

        return if (route.count() == 1 && route.first() == ancestor) {
            emptyList()
        } else {
            route.map { it.name }
        }
    }

    private fun findLowestCommonAncestor(leftPath: List<MapNode>, rightPath: List<MapNode>): MapNode {
        var ancestor = leftPath.first()

        for(i in 0..min(leftPath.count() - 1, rightPath.count() - 1)) {
            if (leftPath[i] == rightPath[i]) {
                ancestor = leftPath[i]
            } else {
                break
            }
        }

        return ancestor
    }

    private fun findRouteFromRootTo(name: String, node: MapNode, path: MutableList<MapNode>): Boolean {
        if (node.name == name) {
            path.add(node)
            return true
        }

        for(child in node.children) {
            if (findRouteFromRootTo(name, child, path)) {
                path.add(0, node)
                return true
            }
        }

        return false
    }
}

data class MapNode(
    val name: String,
    val children: List<MapNode>
)
