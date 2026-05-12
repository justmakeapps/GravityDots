package com.justmakeapps.gravitydots.ui.game

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.*
import com.justmakeapps.gravitydots.R
import com.justmakeapps.gravitydots.ads.AdIds
import com.justmakeapps.gravitydots.ads.AdManager
import com.justmakeapps.gravitydots.data.GamePrefs
import com.justmakeapps.gravitydots.data.LevelRepository
import com.justmakeapps.gravitydots.databinding.FragmentGameBinding

class GameFragment : Fragment(), GameView.GameListener {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefs: GamePrefs
    private lateinit var adManager: AdManager

    private var levelNumber = 1
    private var outOfWallsShown = false
    private var isSimulating = false

    companion object {
        fun newInstance(level: Int) = GameFragment().apply {
            arguments = bundleOf("level" to level)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        levelNumber = arguments?.getInt("level", 1) ?: 1
        prefs = GamePrefs(requireContext())
        adManager = (requireActivity() as? AdManagerProvider)?.adManager
            ?: AdManager(requireContext()).also { it.initialize() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupBannerAd()
        setupLevel()
    }

    // ── Level setup ───────────────────────────────────────────────────────────

    private fun setupLevel() {
        val level = LevelRepository.getLevel(levelNumber) ?: return
        outOfWallsShown = false
        isSimulating = false

        binding.tvLevelTitle.text = "Level $levelNumber"
        binding.gameView.listener = this

        // Reset play/reset button states
        binding.btnPlay.visibility = View.VISIBLE
        binding.btnPlay.isEnabled = true
        binding.btnReset.visibility = View.GONE

        binding.gameView.setupLevel(
            levelDots = level.dots,
            levelHoles = level.holes,
            maxWallCount = level.maxWalls,
            bonusWalls = prefs.bonusWalls
        )

        updateWallsUI(0, level.maxWalls + prefs.bonusWalls)
        buildDotIndicators(level)

        // Buttons
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.btnReset.setOnClickListener {
            setupLevel()
        }

        binding.btnPlay.setOnClickListener {
            if (isSimulating) return@setOnClickListener
            isSimulating = true
            binding.btnPlay.visibility = View.GONE
            binding.btnReset.visibility = View.VISIBLE
            binding.gameView.startSimulation()
        }
    }

    private fun buildDotIndicators(level: com.justmakeapps.gravitydots.data.LevelData) {
        binding.dotIndicators.removeAllViews()
        level.dots.forEach { dot ->
            val size = resources.getDimensionPixelSize(R.dimen.dot_indicator_size)
            val indicator = View(requireContext()).apply {
                layoutParams = ViewGroup.MarginLayoutParams(size, size).apply { marginEnd = 10 }
                background = resources.getDrawable(R.drawable.circle_dot, null)
                backgroundTintList = android.content.res.ColorStateList.valueOf(
                    android.graphics.Color.parseColor(dot.colorHex)
                )
            }
            binding.dotIndicators.addView(indicator)
        }
    }

    private fun setupBannerAd() {
        if (prefs.adsRemoved) {
            binding.bannerContainer.visibility = View.GONE
            return
        }
        val bannerAd = AdView(requireContext()).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = AdIds.BANNER
            loadAd(AdRequest.Builder().build())
        }
        binding.bannerContainer.addView(bannerAd)
    }

    private fun updateWallsUI(used: Int, total: Int) {
        val left = (total - used).coerceAtLeast(0)
        binding.tvWallsLeft.text = "$left walls left"
        binding.wallDots.removeAllViews()
        for (i in 0 until total) {
            val size = resources.getDimensionPixelSize(R.dimen.wall_dot_size)
            val dot = View(requireContext()).apply {
                layoutParams = ViewGroup.MarginLayoutParams(size, size).apply { marginEnd = 6 }
                background = resources.getDrawable(R.drawable.circle_dot, null)
                backgroundTintList = android.content.res.ColorStateList.valueOf(
                    if (i < used) android.graphics.Color.parseColor("#D3D1C7")
                    else android.graphics.Color.parseColor("#1A1A1A")
                )
            }
            binding.wallDots.addView(dot)
        }
    }

    // ── GameView.GameListener ─────────────────────────────────────────────────

    override fun onWallDrawn(wallsUsed: Int, wallsLeft: Int) {
        val level = LevelRepository.getLevel(levelNumber) ?: return
        val total = level.maxWalls + prefs.bonusWalls
        updateWallsUI(wallsUsed, total)
    }

    override fun onLevelComplete(starsEarned: Int) {
        prefs.setStars(levelNumber, starsEarned)
        prefs.unlockLevel(levelNumber + 1)

        val dialog = LevelCompleteDialog.newInstance(
            level = levelNumber,
            stars = starsEarned,
            hasNextLevel = LevelRepository.getLevel(levelNumber + 1) != null
        )
        dialog.onWatchAd = {
            adManager.showRewarded(
                requireActivity(),
                onRewarded = {
                    prefs.addBonusWalls(3)
                    Toast.makeText(requireContext(), "+3 bonus walls saved!", Toast.LENGTH_SHORT).show()
                }
            )
        }
        dialog.onNextLevel = {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, newInstance(levelNumber + 1))
                .addToBackStack(null)
                .commit()
        }
        dialog.onRetry = { setupLevel() }
        dialog.show(childFragmentManager, "complete")
    }

    override fun onLevelFailed() {
        val dialog = LevelFailedDialog.newInstance(levelNumber)
        dialog.onWatchAd = {
            adManager.showRewarded(
                requireActivity(),
                onRewarded = {
                    prefs.addBonusWalls(2)
                    setupLevel()
                    Toast.makeText(requireContext(), "+2 walls added!", Toast.LENGTH_SHORT).show()
                }
            )
        }
        dialog.onRetry = { setupLevel() }
        dialog.show(childFragmentManager, "failed")
    }

    override fun onOutOfWalls() {
        if (outOfWallsShown) return
        outOfWallsShown = true
        val dialog = OutOfWallsDialog()
        dialog.onWatchAd = {
            adManager.showRewarded(
                requireActivity(),
                onRewarded = {
                    outOfWallsShown = false
                    prefs.addBonusWalls(2)
                    binding.gameView.addExtraWalls(2)
                    val level = LevelRepository.getLevel(levelNumber) ?: return@showRewarded
                    updateWallsUI(binding.gameView.wallsUsed, level.maxWalls + prefs.bonusWalls)
                    Toast.makeText(requireContext(), "+2 walls! Keep drawing.", Toast.LENGTH_SHORT).show()
                }
            )
        }
        dialog.onRestart = { setupLevel() }
        dialog.show(childFragmentManager, "out_of_walls")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface AdManagerProvider {
    val adManager: AdManager
}
