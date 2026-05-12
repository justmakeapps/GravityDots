package com.justmakeapps.gravitydots.ui.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.*
import com.justmakeapps.gravitydots.R
import com.justmakeapps.gravitydots.ads.AdIds
import com.justmakeapps.gravitydots.data.GamePrefs
import com.justmakeapps.gravitydots.databinding.FragmentHomeBinding
import com.justmakeapps.gravitydots.ui.game.GameFragment
import com.justmakeapps.gravitydots.ui.levelselect.LevelSelectFragment
import com.justmakeapps.gravitydots.ui.settings.SettingsFragment

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: GamePrefs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prefs = GamePrefs(requireContext())

        setupBannerAd()
        setupLevelGrid()

        val nextLevel = prefs.highestUnlockedLevel
        binding.btnPlay.text = "play level $nextLevel"
        binding.btnPlay.setOnClickListener {
            navigateTo(GameFragment.newInstance(nextLevel))
        }

        binding.btnAllLevels.setOnClickListener {
            navigateTo(LevelSelectFragment())
        }

        binding.btnSettings.setOnClickListener {
            navigateTo(SettingsFragment())
        }
    }

    private fun setupLevelGrid() {
        val unlocked = prefs.highestUnlockedLevel
        val levelViews = listOf(
            binding.level1, binding.level2, binding.level3, binding.level4
        )
        levelViews.forEachIndexed { index, btn ->
            val lvl = index + 1
            val isUnlocked = lvl <= unlocked
            btn.alpha = if (isUnlocked) 1f else 0.35f
            btn.isEnabled = isUnlocked
            btn.setOnClickListener {
                navigateTo(GameFragment.newInstance(lvl))
            }
            // Show stars
            val stars = prefs.getStars(lvl)
            btn.text = "$lvl\n${"★".repeat(stars)}${"☆".repeat(3 - stars)}"
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

    private fun navigateTo(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        setupLevelGrid()
        val nextLevel = prefs.highestUnlockedLevel
        binding.btnPlay.text = "play level $nextLevel"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
