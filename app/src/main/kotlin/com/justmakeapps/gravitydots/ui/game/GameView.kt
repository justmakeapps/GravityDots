package com.justmakeapps.gravitydots.ui.game

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.justmakeapps.gravitydots.utils.*
import kotlin.math.*

class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    interface GameListener {
        fun onWallDrawn(wallsUsed: Int, wallsLeft: Int)
        fun onLevelComplete(starsEarned: Int)
        fun onLevelFailed()
        fun onOutOfWalls()
    }

    var listener: GameListener? = null

    // State
    private val walls = mutableListOf<Wall>()
    private val dots = mutableListOf<SimDot>()
    private val holes = mutableListOf<SimHole>()
    private var maxWalls = 3
    private var extraWalls = 0
    private var isRunning = false
    private var isDrawingPhase = true
    private var gameOver = false

    // Drawing wall in progress
    private var drawStart: PointF? = null
    private var drawCurrent: PointF? = null

    // Physics
    private var physics: PhysicsEngine? = null
    private var lastFrameTime = 0L

    // Paints
    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val holePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }
    private val wallPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1A1A1A")
        strokeWidth = 8f
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
    }
    private val wallPreviewPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#1A1A1A")
        strokeWidth = 6f
        strokeCap = Paint.Cap.ROUND
        style = Paint.Style.STROKE
        alpha = 120
        pathEffect = DashPathEffect(floatArrayOf(20f, 10f), 0f)
    }
    private val hintPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#C0BEB8")
        textSize = 30f
        textAlign = Paint.Align.CENTER
    }
    private val dotShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        alpha = 40
        color = Color.BLACK
    }

    // ── Setup ─────────────────────────────────────────────────────────────────

    fun setupLevel(
        levelDots: List<com.justmakeapps.gravitydots.data.DotData>,
        levelHoles: List<com.justmakeapps.gravitydots.data.HoleData>,
        maxWallCount: Int,
        bonusWalls: Int
    ) {
        walls.clear()
        dots.clear()
        holes.clear()
        maxWalls = maxWallCount
        extraWalls = bonusWalls
        isRunning = false
        isDrawingPhase = true
        gameOver = false
        drawStart = null
        drawCurrent = null

        post {
            val w = width.toFloat()
            val h = height.toFloat()
            physics = PhysicsEngine(w, h)

            levelDots.forEach { d ->
                dots.add(SimDot(
                    id = d.id,
                    color = Color.parseColor(d.colorHex),
                    x = d.startXFraction * w,
                    y = d.startYFraction * h,
                    radius = w * 0.036f
                ))
            }
            levelHoles.forEach { h2 ->
                holes.add(SimHole(
                    color = Color.parseColor(h2.colorHex),
                    x = h2.xFraction * w,
                    y = h2.yFraction * h,
                    radius = w * 0.045f
                ))
            }
            invalidate()
        }
    }

    fun addExtraWalls(count: Int) {
        extraWalls += count
        invalidate()
    }

    val wallsUsed get() = walls.size
    val totalWalls get() = maxWalls + extraWalls

    fun startSimulation() {
        if (isRunning) return
        isDrawingPhase = false
        isRunning = true
        lastFrameTime = System.nanoTime()
        postInvalidate()
    }

    fun resetLevel() {
        isRunning = false
        isDrawingPhase = true
        gameOver = false
        walls.clear()
        drawStart = null
        drawCurrent = null
        dots.forEach { dot ->
            dot.landed = false
            dot.failed = false
            dot.vx = 0f
            dot.vy = 0f
        }
        // reset dot positions
        val w = width.toFloat()
        val h = height.toFloat()
        invalidate()
    }

    // ── Touch ─────────────────────────────────────────────────────────────────

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isDrawingPhase || gameOver) return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                drawStart = PointF(event.x, event.y)
                drawCurrent = PointF(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE -> {
                drawCurrent = PointF(event.x, event.y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                val start = drawStart ?: return true
                val end = PointF(event.x, event.y)
                val dx = end.x - start.x
                val dy = end.y - start.y
                val len = sqrt(dx * dx + dy * dy)
                if (len > 40f) {
                    val used = wallsUsed
                    val total = totalWalls
                    if (used < total) {
                        walls.add(Wall(start.x, start.y, end.x, end.y))
                        listener?.onWallDrawn(walls.size, total - walls.size)
                        if (walls.size >= total) {
                            listener?.onOutOfWalls()
                        }
                    }
                }
                drawStart = null
                drawCurrent = null
                invalidate()
            }
        }
        return true
    }

    // ── Draw ──────────────────────────────────────────────────────────────────

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val w = width.toFloat()
        val h = height.toFloat()

        drawHoles(canvas)
        drawWalls(canvas)
        drawPreviewWall(canvas)
        drawDots(canvas)

        if (isDrawingPhase && walls.isEmpty() && dots.isNotEmpty()) {
            canvas.drawText("draw walls to guide the dots", w / 2f, h * 0.93f, hintPaint)
        }

        if (isRunning) {
            tick()
        }
    }

    private fun drawHoles(canvas: Canvas) {
        for (hole in holes) {
            holePaint.color = hole.color
            holePaint.strokeWidth = 5f
            canvas.drawCircle(hole.x, hole.y, hole.radius, holePaint)
            // inner dot marker
            val innerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = hole.color
                alpha = 40
                style = Paint.Style.FILL
            }
            canvas.drawCircle(hole.x, hole.y, hole.radius * 0.45f, innerPaint)
        }
    }

    private fun drawWalls(canvas: Canvas) {
        for (wall in walls) {
            canvas.drawLine(wall.startX, wall.startY, wall.endX, wall.endY, wallPaint)
        }
    }

    private fun drawPreviewWall(canvas: Canvas) {
        val s = drawStart ?: return
        val e = drawCurrent ?: return
        canvas.drawLine(s.x, s.y, e.x, e.y, wallPreviewPaint)
    }

    private fun drawDots(canvas: Canvas) {
        for (dot in dots) {
            if (dot.failed) continue
            // shadow
            canvas.drawCircle(dot.x + 4f, dot.y + 4f, dot.radius, dotShadowPaint)
            // dot
            dotPaint.color = dot.color
            dotPaint.alpha = if (dot.landed) 180 else 255
            canvas.drawCircle(dot.x, dot.y, dot.radius, dotPaint)
            // shine
            val shinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                alpha = 80
                style = Paint.Style.FILL
            }
            canvas.drawCircle(dot.x - dot.radius * 0.3f, dot.y - dot.radius * 0.3f, dot.radius * 0.35f, shinePaint)
        }
    }

    // ── Physics tick ──────────────────────────────────────────────────────────

    private fun tick() {
        if (!isRunning || gameOver) return
        val now = System.nanoTime()
        val dt = ((now - lastFrameTime) / 1_000_000_000f).coerceAtMost(0.032f)
        lastFrameTime = now

        physics?.step(dots, walls, holes, dt)

        val activeDots = dots.filter { !it.landed && !it.failed }
        val failedDots = dots.filter { it.failed }
        val landedDots = dots.filter { it.landed }

        when {
            failedDots.isNotEmpty() && !gameOver -> {
                gameOver = true
                isRunning = false
                postDelayed({ listener?.onLevelFailed() }, 500)
            }
            activeDots.isEmpty() && failedDots.isEmpty() && landedDots.size == dots.size -> {
                gameOver = true
                isRunning = false
                val wallsRemaining = totalWalls - wallsUsed
                val stars = when {
                    wallsRemaining >= 2 -> 3
                    wallsRemaining == 1 -> 2
                    else -> 1
                }
                postDelayed({ listener?.onLevelComplete(stars) }, 600)
            }
        }

        invalidate()
    }
}
