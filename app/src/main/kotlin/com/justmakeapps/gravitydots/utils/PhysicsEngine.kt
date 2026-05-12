package com.justmakeapps.gravitydots.utils

import android.graphics.Color
import android.graphics.PointF
import android.graphics.RectF
import kotlin.math.*

data class Wall(
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float
)

data class SimDot(
    val id: Int,
    val color: Int,
    var x: Float,
    var y: Float,
    var vx: Float = 0f,
    var vy: Float = 0f,
    var landed: Boolean = false,
    var failed: Boolean = false,
    val radius: Float = 22f
)

data class SimHole(
    val color: Int,
    val x: Float,
    val y: Float,
    val radius: Float = 26f
)

class PhysicsEngine(
    private val canvasWidth: Float,
    private val canvasHeight: Float
) {
    companion object {
        const val GRAVITY = 980f      // px/s²
        const val FRICTION = 0.55f    // wall bounce damping
        const val WALL_THICKNESS = 8f
    }

    fun step(
        dots: List<SimDot>,
        walls: List<Wall>,
        holes: List<SimHole>,
        dt: Float                     // seconds
    ) {
        for (dot in dots) {
            if (dot.landed || dot.failed) continue

            // gravity
            dot.vy += GRAVITY * dt

            // integrate
            dot.x += dot.vx * dt
            dot.y += dot.vy * dt

            // wall collisions
            for (wall in walls) resolveWall(dot, wall)

            // canvas bounds
            if (dot.x - dot.radius < 0) { dot.x = dot.radius; dot.vx = abs(dot.vx) * FRICTION }
            if (dot.x + dot.radius > canvasWidth) { dot.x = canvasWidth - dot.radius; dot.vx = -abs(dot.vx) * FRICTION }

            // hole detection
            for (hole in holes) {
                val dx = dot.x - hole.x
                val dy = dot.y - hole.y
                val dist = sqrt(dx * dx + dy * dy)
                if (dist < hole.radius + dot.radius * 0.5f) {
                    if (dot.color == hole.color) {
                        dot.landed = true
                        dot.x = hole.x
                        dot.y = hole.y
                        dot.vx = 0f
                        dot.vy = 0f
                    } else {
                        // wrong hole — bounce away
                        dot.vy = -abs(dot.vy) * FRICTION
                        dot.vx *= FRICTION
                    }
                }
            }

            // fell off bottom
            if (dot.y - dot.radius > canvasHeight) {
                dot.failed = true
            }
        }
    }

    private fun resolveWall(dot: SimDot, wall: Wall) {
        val wx = wall.endX - wall.startX
        val wy = wall.endY - wall.startY
        val len = sqrt(wx * wx + wy * wy)
        if (len < 1f) return

        val nx = -wy / len   // normal
        val ny = wx / len

        val dx = dot.x - wall.startX
        val dy = dot.y - wall.startY

        val proj = dx * (wx / len) + dy * (wy / len)
        val clampedProj = proj.coerceIn(0f, len)

        val closestX = wall.startX + (wx / len) * clampedProj
        val closestY = wall.startY + (wy / len) * clampedProj

        val sepX = dot.x - closestX
        val sepY = dot.y - closestY
        val sepDist = sqrt(sepX * sepX + sepY * sepY)

        val minDist = dot.radius + WALL_THICKNESS / 2f
        if (sepDist < minDist && sepDist > 0.01f) {
            // push dot out
            val overlap = minDist - sepDist
            dot.x += (sepX / sepDist) * overlap
            dot.y += (sepY / sepDist) * overlap

            // reflect velocity along normal
            val velDotN = dot.vx * (sepX / sepDist) + dot.vy * (sepY / sepDist)
            if (velDotN < 0) {
                dot.vx -= (1f + FRICTION) * velDotN * (sepX / sepDist)
                dot.vy -= (1f + FRICTION) * velDotN * (sepY / sepDist)
                dot.vx *= 0.85f
                dot.vy *= 0.85f
            }
        }
    }
}
