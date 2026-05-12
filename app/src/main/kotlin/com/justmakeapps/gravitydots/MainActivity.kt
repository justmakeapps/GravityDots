package com.justmakeapps.gravitydots

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.justmakeapps.gravitydots.ads.AdManager
import com.justmakeapps.gravitydots.databinding.ActivityMainBinding
import com.justmakeapps.gravitydots.ui.game.AdManagerProvider
import com.justmakeapps.gravitydots.ui.home.HomeFragment

class MainActivity : AppCompatActivity(), AdManagerProvider {

    override val adManager: AdManager by lazy { AdManager(this) }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adManager.initialize()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }
    }
}
