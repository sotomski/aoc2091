package day03

import kotlin.math.abs
import kotlin.math.min

class Wire(private val turns: List<Point>) {
    private val points: List<Point>

    private val metric = ManhattanMetric()

    init {
        points = turns
            .windowed(2, 1) { pair -> pointsBetween(pair[0], pair[1]) }
            .flatten()
    }

    private fun pointsBetween(start: Point, end: Point): List<Point> {
        return if (start.x == end.x) {
            val startY = min(start.y, end.y) + 1
            val endY = Integer.max(start.y, end.y)
            (startY..endY).map { Point(start.x, it) }
        } else {
            val startX = min(start.x, end.x) + 1
            val endX = Integer.max(start.x, end.x)
            (startX..endX).map { Point(it, start.y) }
        }
    }

    fun intersect(other: Wire): List<Point> {
        return points.toSet().intersect(other.points.toSet()).sortedBy { it.distanceFromOrigin() }
    }

    private fun Point.distanceFromOrigin() = metric.distanceBetween(this, Point.ZERO)

    fun distanceToClosestIntersectionWith(other: Wire) = intersect(other).first().distanceFromOrigin()

    fun stepsTo(point: Point): Int {
        var traversedDistance = 0
        var shouldBreak = false

        for (segment in turns.windowed(2, 1)) {
            val start = segment[0]
            val end = segment[1]

            traversedDistance += if (!pointsBetween(start, end).contains(point)) {
                metric.distanceBetween(start, end)
            } else {
                shouldBreak = true
                metric.distanceBetween(start, point)
            }

            if (shouldBreak) break
        }

        return traversedDistance
    }

    companion object {
        fun parse(input: String, origin: Point = Point.ZERO): Wire {
            val wire = mutableListOf(origin)

            for (step in input.split(',')) {
                val last = wire.last()
                val new = when (step[0]) {
                    'L' -> last.copy(x = last.x - step.substring(1).toInt())
                    'U' -> last.copy(y = last.y + step.substring(1).toInt())
                    'R' -> last.copy(x = last.x + step.substring(1).toInt())
                    'D' -> last.copy(y = last.y - step.substring(1).toInt())
                    else -> throw IllegalArgumentException()
                }

                wire.add(new)
            }

            return Wire(wire)
        }
    }
}

data class Point(val x: Int, val y: Int) {
    companion object {
        val ZERO = Point(0, 0)
    }
}

class ManhattanMetric {
    fun distanceBetween(start: Point, end: Point) = abs(start.x - end.x) + abs(start.y - end.y)
}