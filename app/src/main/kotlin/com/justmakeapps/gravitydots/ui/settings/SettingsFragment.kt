package com.justmakeapps.gravitydots.ui.settings

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.justmakeapps.gravitydots.data.GamePrefs
import com.justmakeapps.gravitydots.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: GamePrefs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        prefs = GamePrefs(requireContext())

        binding.btnBack.setOnClickListener { parentFragmentManager.popBackStack() }

        binding.switchSound.isChecked = prefs.getSoundEnabled()
        binding.switchHaptic.isChecked = prefs.getHapticEnabled()
        binding.switchHints.isChecked = prefs.getHintsEnabled()

        binding.switchSound.setOnCheckedChangeListener { _, checked -> prefs.setSoundEnabled(checked) }
        binding.switchHaptic.setOnCheckedChangeListener { _, checked -> prefs.setHapticEnabled(checked) }
        binding.switchHints.setOnCheckedChangeListener { _, checked -> prefs.setHintsEnabled(checked) }

        binding.btnRemoveAds.setOnClickListener {
            // In production: launch billing flow here
            prefs.adsRemoved = true
            Toast.makeText(requireContext(), "Ads removed! Thank you.", Toast.LENGTH_SHORT).show()
            binding.btnRemoveAds.isEnabled = false
            binding.btnRemoveAds.alpha = 0.5f
        }

        binding.btnRestorePurchases.setOnClickListener {
            Toast.makeText(requireContext(), "Checking purchases…", Toast.LENGTH_SHORT).show()
        }

        binding.btnResetProgress.setOnClickListener {
            prefs.highestUnlockedLevel = 1
            prefs.bonusWalls = 0
            Toast.makeText(requireContext(), "Progress reset.", Toast.LENGTH_SHORT).show()
        }

        if (prefs.adsRemoved) {
            binding.btnRemoveAds.isEnabled = false
            binding.btnRemoveAds.alpha = 0.5f
            binding.btnRemoveAds.text = "ads removed"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Extend GamePrefs for settings
fun GamePrefs.getSoundEnabled(): Boolean =
    (this as? Any)?.let {
        val field = it.javaClass.getDeclaredField("prefs")
        field.isAccessible = true
        val sp = field.get(it) as android.content.SharedPreferences
        sp.getBoolean("sound", true)
    } ?: true

fun GamePrefs.setSoundEnabled(v: Boolean) {
    val field = this.javaClass.getDeclaredField("prefs")
    field.isAccessible = true
    val sp = field.get(this) as android.content.SharedPreferences
    sp.edit().putBoolean("sound", v).apply()
}

fun GamePrefs.getHapticEnabled(): Boolean {
    val field = this.javaClass.getDeclaredField("prefs")
    field.isAccessible = true
    val sp = field.get(this) as android.content.SharedPreferences
    return sp.getBoolean("haptic", false)
}

fun GamePrefs.setHapticEnabled(v: Boolean) {
    val field = this.javaClass.getDeclaredField("prefs")
    field.isAccessible = true
    val sp = field.get(this) as android.content.SharedPreferences
    sp.edit().putBoolean("haptic", v).apply()
}

fun GamePrefs.getHintsEnabled(): Boolean {
    val field = this.javaClass.getDeclaredField("prefs")
    field.isAccessible = true
    val sp = field.get(this) as android.content.SharedPreferences
    return sp.getBoolean("hints", true)
}

fun GamePrefs.setHintsEnabled(v: Boolean) {
    val field = this.javaClass.getDeclaredField("prefs")
    field.isAccessible = true
    val sp = field.get(this) as android.content.SharedPreferences
    sp.edit().putBoolean("hints", v).apply()
}
