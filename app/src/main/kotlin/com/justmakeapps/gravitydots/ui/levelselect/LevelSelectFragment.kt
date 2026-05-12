package com.justmakeapps.gravitydots.ui.levelselect

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.*
import com.justmakeapps.gravitydots.R
import com.justmakeapps.gravitydots.ads.AdIds
import com.justmakeapps.gravitydots.ads.AdManager
import com.justmakeapps.gravitydots.data.GamePrefs
import com.justmakeapps.gravitydots.data.LevelRepository
import com.justmakeapps.gravitydots.databinding.FragmentLevelSelectBinding
import com.justmakeapps.gravitydots.ui.game.AdManagerProvider
import com.justmakeapps.gravitydots.ui.game.GameFragment

class LevelSelectFragment : Fragment() {

    private var _binding: FragmentLevelSelectBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: GamePrefs
    private lateinit var adManager: AdManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLevelSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prefs = GamePrefs(requireContext())
        adManager = (requireActivity() as? AdManagerProvider)?.adManager
            ?: AdManager(requireContext()).also { it.initialize() }

        setupBannerAd()
        setupLevelGrid()

        binding.btnBack.setOnClickListener { parentFragmentManager.popBackStack() }

        binding.btnUnlockLevels.setOnClickListener {
            adManager.showRewarded(
                requireActivity(),
                onRewarded = {
                    val current = prefs.highestUnlockedLevel
                    prefs.unlockLevel(current + 3)
                    setupLevelGrid()
                    Toast.makeText(requireContext(), "3 levels unlocked!", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun setupLevelGrid() {
        val unlocked = prefs.highestUnlockedLevel
        val grid = binding.levelGrid
        grid.removeAllViews()

        val colCount = 5
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val padding = (14 * displayMetrics.density * 2).toInt()
        val spacing = (6 * displayMetrics.density).toInt()
        val cellSize = (screenWidth - padding - spacing * (colCount - 1)) / colCount

        LevelRepository.levels.forEach { level ->
            val isUnlocked = level.number <= unlocked
            val stars = prefs.getStars(level.number)

            val btn = Button(requireContext()).apply {
                val lp = GridLayout.LayoutParams().apply {
                    width = cellSize
                    height = cellSize
                    setMargins(3, 3, 3, 3)
                }

                layoutParams = lp
                text = buildString {
                    append(level.number.toString())
                    append("\n")
                    append("★".repeat(stars))
                    append("☆".repeat(3 - stars))
                }
                textSize = 11f
                alpha = if (isUnlocked) 1f else 0.35f
                isEnabled = isUnlocked
                setBackgroundResource(
                    if (isUnlocked) R.drawable.btn_level_unlocked
                    else R.drawable.btn_level_locked
                )
                setTextColor(
                    if (isUnlocked) android.graphics.Color.WHITE
                    else android.graphics.Color.parseColor("#888780")
                )
                setOnClickListener {
                    if (!isUnlocked) return@setOnClickListener
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, GameFragment.newInstance(level.number))
                        .addToBackStack(null)
                        .commit()
                }
            }
            grid.addView(btn)
        }
    }

    private fun setupBannerAd() {
        if (prefs.adsRemoved) {
            binding.bannerContainer.visibility = View.GONE
            return
        }
        val banner = AdView(requireContext()).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = AdIds.BANNER
            loadAd(AdRequest.Builder().build())
        }
        binding.bannerContainer.addView(banner)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
