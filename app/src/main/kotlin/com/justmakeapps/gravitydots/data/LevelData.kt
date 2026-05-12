package com.justmakeapps.gravitydots.data

data class DotData(
    val id: Int,
    val colorHex: String,
    val startXFraction: Float,
    val startYFraction: Float
)

data class HoleData(
    val colorHex: String,
    val xFraction: Float,
    val yFraction: Float
)

data class LevelData(
    val number: Int,
    val maxWalls: Int,
    val dots: List<DotData>,
    val holes: List<HoleData>
)

object LevelRepository {

    val levels: List<LevelData> = listOf(

        // ── Tutorial ───────────────────────────────────────────────
        LevelData(1, maxWalls = 1,
            dots  = listOf(DotData(0, "#534AB7", 0.5f, 0.08f)),
            holes = listOf(HoleData("#534AB7", 0.5f, 0.82f))
        ),
        LevelData(2, maxWalls = 2,
            dots  = listOf(DotData(0, "#534AB7", 0.25f, 0.08f), DotData(1, "#D85A30", 0.70f, 0.08f)),
            holes = listOf(HoleData("#534AB7", 0.20f, 0.82f), HoleData("#D85A30", 0.75f, 0.82f))
        ),
        LevelData(3, maxWalls = 2,
            dots  = listOf(DotData(0, "#534AB7", 0.20f, 0.07f), DotData(1, "#D85A30", 0.55f, 0.07f), DotData(2, "#1D9E75", 0.80f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.15f, 0.82f), HoleData("#D85A30", 0.50f, 0.82f), HoleData("#1D9E75", 0.82f, 0.82f))
        ),

        // ── Easy ───────────────────────────────────────────────────
        LevelData(4, maxWalls = 3,
            dots  = listOf(DotData(0, "#534AB7", 0.30f, 0.07f), DotData(1, "#D85A30", 0.65f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.75f, 0.82f), HoleData("#D85A30", 0.18f, 0.82f))
        ),
        LevelData(5, maxWalls = 3,
            dots  = listOf(DotData(0, "#534AB7", 0.15f, 0.07f), DotData(1, "#D85A30", 0.50f, 0.07f), DotData(2, "#1D9E75", 0.82f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.82f, 0.82f), HoleData("#D85A30", 0.18f, 0.82f), HoleData("#1D9E75", 0.50f, 0.82f))
        ),
        LevelData(6, maxWalls = 3,
            dots  = listOf(DotData(0, "#534AB7", 0.10f, 0.07f), DotData(1, "#D85A30", 0.85f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.50f, 0.82f), HoleData("#D85A30", 0.50f, 0.50f))
        ),
        LevelData(7, maxWalls = 4,
            dots  = listOf(DotData(0, "#534AB7", 0.20f, 0.07f), DotData(1, "#D85A30", 0.50f, 0.07f), DotData(2, "#1D9E75", 0.80f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.80f, 0.82f), HoleData("#D85A30", 0.50f, 0.82f), HoleData("#1D9E75", 0.20f, 0.82f))
        ),
        LevelData(8, maxWalls = 4,
            dots  = listOf(DotData(0, "#534AB7", 0.10f, 0.07f), DotData(1, "#D85A30", 0.45f, 0.07f), DotData(2, "#1D9E75", 0.75f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.88f, 0.82f), HoleData("#D85A30", 0.12f, 0.82f), HoleData("#1D9E75", 0.50f, 0.82f))
        ),

        // ── Medium ─────────────────────────────────────────────────
        LevelData(9, maxWalls = 4,
            dots  = listOf(DotData(0, "#534AB7", 0.15f, 0.07f), DotData(1, "#D85A30", 0.85f, 0.07f), DotData(2, "#1D9E75", 0.50f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.50f, 0.82f), HoleData("#D85A30", 0.15f, 0.82f), HoleData("#1D9E75", 0.85f, 0.82f))
        ),
        LevelData(10, maxWalls = 4,
            dots  = listOf(DotData(0, "#534AB7", 0.10f, 0.07f), DotData(1, "#D85A30", 0.50f, 0.07f), DotData(2, "#1D9E75", 0.88f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.88f, 0.60f), HoleData("#D85A30", 0.50f, 0.82f), HoleData("#1D9E75", 0.10f, 0.60f))
        ),
        LevelData(11, maxWalls = 5,
            dots  = listOf(DotData(0, "#534AB7", 0.20f, 0.07f), DotData(1, "#D85A30", 0.55f, 0.07f), DotData(2, "#1D9E75", 0.80f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.80f, 0.45f), HoleData("#D85A30", 0.20f, 0.82f), HoleData("#1D9E75", 0.50f, 0.82f))
        ),
        LevelData(12, maxWalls = 5,
            dots  = listOf(DotData(0, "#534AB7", 0.12f, 0.07f), DotData(1, "#D85A30", 0.50f, 0.07f), DotData(2, "#1D9E75", 0.85f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.85f, 0.82f), HoleData("#D85A30", 0.50f, 0.40f), HoleData("#1D9E75", 0.12f, 0.82f))
        ),
        LevelData(13, maxWalls = 5,
            dots  = listOf(DotData(0, "#534AB7", 0.30f, 0.07f), DotData(1, "#D85A30", 0.70f, 0.07f), DotData(2, "#1D9E75", 0.50f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.10f, 0.82f), HoleData("#D85A30", 0.90f, 0.82f), HoleData("#1D9E75", 0.50f, 0.65f))
        ),
        LevelData(14, maxWalls = 5,
            dots  = listOf(DotData(0, "#534AB7", 0.10f, 0.07f), DotData(1, "#D85A30", 0.50f, 0.07f), DotData(2, "#1D9E75", 0.88f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.30f, 0.82f), HoleData("#D85A30", 0.88f, 0.40f), HoleData("#1D9E75", 0.10f, 0.82f))
        ),
        LevelData(15, maxWalls = 5,
            dots  = listOf(DotData(0, "#534AB7", 0.15f, 0.07f), DotData(1, "#D85A30", 0.50f, 0.07f), DotData(2, "#1D9E75", 0.80f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.80f, 0.82f), HoleData("#D85A30", 0.15f, 0.82f), HoleData("#1D9E75", 0.50f, 0.50f))
        ),

        // ── Hard ───────────────────────────────────────────────────
        LevelData(16, maxWalls = 5,
            dots  = listOf(DotData(0, "#534AB7", 0.10f, 0.07f), DotData(1, "#D85A30", 0.35f, 0.07f), DotData(2, "#1D9E75", 0.65f, 0.07f), DotData(3, "#EF9F27", 0.88f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.88f, 0.82f), HoleData("#D85A30", 0.65f, 0.82f), HoleData("#1D9E75", 0.35f, 0.82f), HoleData("#EF9F27", 0.10f, 0.82f))
        ),
        LevelData(17, maxWalls = 6,
            dots  = listOf(DotData(0, "#534AB7", 0.15f, 0.07f), DotData(1, "#D85A30", 0.50f, 0.07f), DotData(2, "#1D9E75", 0.82f, 0.07f), DotData(3, "#EF9F27", 0.33f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.50f, 0.82f), HoleData("#D85A30", 0.82f, 0.55f), HoleData("#1D9E75", 0.15f, 0.82f), HoleData("#EF9F27", 0.82f, 0.82f))
        ),
        LevelData(18, maxWalls = 6,
            dots  = listOf(DotData(0, "#534AB7", 0.12f, 0.07f), DotData(1, "#D85A30", 0.40f, 0.07f), DotData(2, "#1D9E75", 0.68f, 0.07f), DotData(3, "#EF9F27", 0.88f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.88f, 0.55f), HoleData("#D85A30", 0.12f, 0.82f), HoleData("#1D9E75", 0.50f, 0.82f), HoleData("#EF9F27", 0.30f, 0.55f))
        ),
        LevelData(19, maxWalls = 6,
            dots  = listOf(DotData(0, "#534AB7", 0.20f, 0.07f), DotData(1, "#D85A30", 0.50f, 0.07f), DotData(2, "#1D9E75", 0.78f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.78f, 0.35f), HoleData("#D85A30", 0.10f, 0.82f), HoleData("#1D9E75", 0.50f, 0.60f))
        ),
        LevelData(20, maxWalls = 6,
            dots  = listOf(DotData(0, "#534AB7", 0.10f, 0.07f), DotData(1, "#D85A30", 0.35f, 0.07f), DotData(2, "#1D9E75", 0.65f, 0.07f), DotData(3, "#EF9F27", 0.88f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.65f, 0.55f), HoleData("#D85A30", 0.88f, 0.82f), HoleData("#1D9E75", 0.10f, 0.82f), HoleData("#EF9F27", 0.35f, 0.82f))
        ),

        // ── Expert ─────────────────────────────────────────────────
        LevelData(21, maxWalls = 6,
            dots  = listOf(DotData(0, "#534AB7", 0.15f, 0.07f), DotData(1, "#D85A30", 0.40f, 0.07f), DotData(2, "#1D9E75", 0.65f, 0.07f), DotData(3, "#EF9F27", 0.88f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.88f, 0.35f), HoleData("#D85A30", 0.15f, 0.55f), HoleData("#1D9E75", 0.50f, 0.82f), HoleData("#EF9F27", 0.20f, 0.82f))
        ),
        LevelData(22, maxWalls = 7,
            dots  = listOf(DotData(0, "#534AB7", 0.10f, 0.07f), DotData(1, "#D85A30", 0.33f, 0.07f), DotData(2, "#1D9E75", 0.58f, 0.07f), DotData(3, "#EF9F27", 0.82f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.82f, 0.82f), HoleData("#D85A30", 0.58f, 0.45f), HoleData("#1D9E75", 0.10f, 0.82f), HoleData("#EF9F27", 0.33f, 0.82f))
        ),
        LevelData(23, maxWalls = 7,
            dots  = listOf(DotData(0, "#534AB7", 0.12f, 0.07f), DotData(1, "#D85A30", 0.38f, 0.07f), DotData(2, "#1D9E75", 0.62f, 0.07f), DotData(3, "#EF9F27", 0.88f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.62f, 0.82f), HoleData("#D85A30", 0.88f, 0.45f), HoleData("#1D9E75", 0.38f, 0.55f), HoleData("#EF9F27", 0.12f, 0.82f))
        ),
        LevelData(24, maxWalls = 7,
            dots  = listOf(DotData(0, "#534AB7", 0.10f, 0.07f), DotData(1, "#D85A30", 0.32f, 0.07f), DotData(2, "#1D9E75", 0.55f, 0.07f), DotData(3, "#EF9F27", 0.78f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.78f, 0.55f), HoleData("#D85A30", 0.10f, 0.45f), HoleData("#1D9E75", 0.88f, 0.82f), HoleData("#EF9F27", 0.50f, 0.82f))
        ),
        LevelData(25, maxWalls = 7,
            dots  = listOf(DotData(0, "#534AB7", 0.10f, 0.07f), DotData(1, "#D85A30", 0.30f, 0.07f), DotData(2, "#1D9E75", 0.55f, 0.07f), DotData(3, "#EF9F27", 0.78f, 0.07f)),
            holes = listOf(HoleData("#534AB7", 0.55f, 0.40f), HoleData("#D85A30", 0.88f, 0.82f), HoleData("#1D9E75", 0.10f, 0.82f), HoleData("#EF9F27", 0.30f, 0.55f))
        )
    )

    fun getLevel(number: Int): LevelData? = levels.find { it.number == number }
}
