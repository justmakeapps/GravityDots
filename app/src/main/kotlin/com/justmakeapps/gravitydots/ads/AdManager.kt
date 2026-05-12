package com.justmakeapps.gravitydots.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.*
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

// Replace test IDs with real ones before publishing
object AdIds {
    const val BANNER    = "ca-app-pub-3307436672128190/8185634433"
    const val REWARDED  = "ca-app-pub-3307436672128190/3517201078"
}

class AdManager(private val context: Context) {

    private var rewardedAd: RewardedAd? = null
    private var isLoadingRewarded = false

    fun initialize() {
        MobileAds.initialize(context) {}
        loadRewarded()
    }

    // ── Rewarded ──────────────────────────────────────────────────────────────

    fun loadRewarded() {
        if (isLoadingRewarded || rewardedAd != null) return
        isLoadingRewarded = true
        val req = AdRequest.Builder().build()
        RewardedAd.load(context, AdIds.REWARDED, req, object : RewardedAdLoadCallback() {
            override fun onAdLoaded(ad: RewardedAd) {
                rewardedAd = ad
                isLoadingRewarded = false
                Log.d("AdManager", "Rewarded ad loaded")
            }
            override fun onAdFailedToLoad(error: LoadAdError) {
                rewardedAd = null
                isLoadingRewarded = false
                Log.d("AdManager", "Rewarded ad failed: ${error.message}")
            }
        })
    }

    fun isRewardedReady(): Boolean = rewardedAd != null

    /**
     * Show rewarded ad.
     * [onRewarded] called when user earns the reward.
     * [onDismissed] called when ad closes (rewarded or not).
     */
    fun showRewarded(
        activity: Activity,
        onRewarded: () -> Unit,
        onDismissed: () -> Unit = {}
    ) {
        val ad = rewardedAd
        if (ad == null) {
            // Ad not ready — still give reward in dev/test to not block gameplay
            onRewarded()
            onDismissed()
            loadRewarded()
            return
        }
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                rewardedAd = null
                loadRewarded()
                onDismissed()
            }
            override fun onAdFailedToShowFullScreenContent(error: AdError) {
                rewardedAd = null
                loadRewarded()
                onDismissed()
            }
        }
        ad.show(activity) { onRewarded() }
    }
}
