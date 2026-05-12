# Gravity Dots — Android (Kotlin)

A minimal physics puzzle game. Draw walls to guide colored dots into matching holes.

---

## Setup

1. Open Android Studio → **File > Open** → select `GravityDots` folder
2. Wait for Gradle sync
3. Run on emulator or device

---

## Ad Logic

| Ad type | When it shows | Reward |
|---|---|---|
| Banner | Always at bottom of every screen | Passive, no interaction |
| Rewarded | Player taps "out of walls" → Watch ad | +2 walls to continue current level |
| Rewarded | Level complete screen → optional | +3 bonus walls saved for any future level |
| Rewarded | Level select → unlock extra levels | +3 levels unlocked |

Ads are **never shown automatically** — always triggered by player choice.

---

## Replace AdMob IDs before publishing

**AndroidManifest.xml:**
```xml
android:value="ca-app-pub-YOUR_APP_ID"
```

**AdManager.kt:**
```kotlin
const val BANNER   = "ca-app-pub-YOUR_ID/YOUR_BANNER_UNIT"
const val REWARDED = "ca-app-pub-YOUR_ID/YOUR_REWARDED_UNIT"
```

Current values are Google's **test IDs** — safe for development.

---

## Project structure

```
GravityDots/
└── app/src/main/
    ├── kotlin/com/gravitydots/app/
    │   ├── MainActivity.kt              ← app entry point
    │   ├── ads/AdManager.kt             ← banner + rewarded logic
    │   ├── data/
    │   │   ├── LevelData.kt             ← 8 levels defined here
    │   │   └── GamePrefs.kt             ← SharedPreferences helper
    │   ├── utils/PhysicsEngine.kt       ← gravity + wall collision
    │   └── ui/
    │       ├── game/
    │       │   ├── GameView.kt          ← canvas: draw walls, simulate dots
    │       │   ├── GameFragment.kt      ← gameplay screen
    │       │   └── GameDialogs.kt       ← complete / failed / out-of-walls dialogs
    │       ├── home/HomeFragment.kt
    │       ├── levelselect/LevelSelectFragment.kt
    │       └── settings/SettingsFragment.kt
    └── res/
        ├── layout/                      ← all XML layouts
        ├── drawable/                    ← shapes, buttons, icons
        └── values/                      ← colors, strings, themes, dimens
```

---

## Adding more levels

Open `data/LevelData.kt` and add to the `levels` list:

```kotlin
LevelData(
    number = 9,
    maxWalls = 5,
    dots = listOf(
        DotData(0, "#534AB7", 0.2f, 0.08f),
        DotData(1, "#D85A30", 0.6f, 0.06f),
    ),
    holes = listOf(
        HoleData("#534AB7", 0.7f, 0.82f),
        HoleData("#D85A30", 0.3f, 0.82f),
    )
)
```

Dot/hole positions are fractions (0.0 to 1.0) of the canvas size.
